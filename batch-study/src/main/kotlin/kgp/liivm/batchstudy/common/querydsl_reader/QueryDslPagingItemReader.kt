package kgp.liivm.batchstudy.common.querydsl_reader

import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import kgp.liivm.batchstudy.common.querydsl_reader.options.QuerydslNoOffsetOptions
import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.util.ClassUtils
import org.springframework.util.CollectionUtils
import java.lang.reflect.Method
import java.util.concurrent.CopyOnWriteArrayList
import java.util.function.Function


class QueryDslPagingItemReader<T>(
    entityManagerFactory: EntityManagerFactory,
    private val identifier: NumberPath<Long>,
    private val method: Method,
    private val options: QuerydslNoOffsetOptions<T>
) : AbstractPagingItemReader<T>() {
    private var transacted = true
    private var queryFunction: Function<JPAQueryFactory, JPAQuery<T>>? = null
    private var entityManager: EntityManager
    private var lastIndex: Long = 0

    init {
        this.name = ClassUtils.getShortName(QueryDslPagingItemReader::class.java)
        this.entityManager = entityManagerFactory.createEntityManager()
    }

    fun queryFunction(query: Function<JPAQueryFactory, JPAQuery<T>>) {
        queryFunction = query
    }

    private fun createQuery(): JPAQuery<T> {
        requireNotNull(queryFunction) { "queryFunction is null" }

        val jpaQueryFactory = JPAQueryFactory(entityManager)
        val query = queryFunction!!.apply(jpaQueryFactory)
        options.initKeys(query, page)

        return options.createQuery(query, page)
    }

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        super.afterPropertiesSet()
    }

    override fun doReadPage() {

        if (transacted) {
            entityManager.clear()
        }

        val query: JPAQuery<T> = createQuery()
            .where(identifier.gt(lastIndex))
            .limit(pageSize.toLong())

        initResults()

        val queryResult = query.fetch()

        results.addAll(queryResult)
        resetCurrentIdIfNotLastPage()
//        setLastIndex(queryResult.last())
    }

    private fun initResults() {
        if (this.results == null) {
            this.results = CopyOnWriteArrayList()
        } else {
            results.clear()
        }
    }

    private fun setLastIndex(entity: T) {
        lastIndex = method.invoke(entity) as Long
    }

    private fun resetCurrentIdIfNotLastPage() {
        if (isNotEmptyResults()) {
            options.resetCurrentId(getLastItem())
        }
    }

    private fun isNotEmptyResults(): Boolean {
        return !CollectionUtils.isEmpty(results) && results[0] != null
    }

    private fun getLastItem(): T {
        return results[results.size - 1]
    }

    @Throws(java.lang.Exception::class)
    override fun doClose() {
        entityManager.close()
        super.doClose()
    }

}
