package kgp.liivm.batchstudy.common.querydsl_reader.options

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.jpa.impl.JPAQuery
import kgp.liivm.batchstudy.common.querydsl_reader.expression.Expression


class QuerydslNoOffsetLongOptions<T>(
    private val field: NumberPath<Long>,
    expression: Expression
) : QuerydslNoOffsetOptions<T>(field, expression) {
    private var currentId: Long? = null
    private var lastId: Long? = null

    override fun initKeys(query: JPAQuery<T>, page: Int) {
        if (page == 0) {
            initFirstId(query)
            initLastId(query)

            println("First Key= $currentId, Last Key= $lastId")
        }
    }

    override fun initFirstId(query: JPAQuery<T>) {
        val clone = query.clone()
        val isGroupByQuery = isGroupByQuery(clone)

        currentId = if (isGroupByQuery) {
            clone
                .select(field)
                .orderBy(if (expression.isAsc()) field.asc() else field.desc())
                .fetchFirst()
        } else {
            clone
                .select(if (expression.isAsc()) field.min() else field.max())
                .fetchFirst()
        }
    }

    override fun initLastId(query: JPAQuery<T>) {
        val clone = query.clone()
        val isGroupByQuery = isGroupByQuery(clone)

        lastId = if (isGroupByQuery) {
            clone
                .select(field)
                .orderBy(if (expression.isAsc()) field.desc() else field.asc())
                .fetchFirst()
        } else {
            clone
                .select(if (expression.isAsc()) field.max() else field.min())
                .fetchFirst()
        }
    }

    override fun createQuery(query: JPAQuery<T>, page: Int): JPAQuery<T> {
        if (currentId == null) {
            return query
        }

        return query
            .where(whereExpression(page))
            .orderBy(orderExpression())
    }

    private fun whereExpression(page: Int): BooleanExpression {
        requireNotNull(currentId) { "current id is null" }

        return expression.where(field, page, currentId!!)
            .and(if (expression.isAsc()) field.loe(lastId) else field.goe(lastId))
    }

    private fun orderExpression(): OrderSpecifier<Long> {
        return expression.order(field)
    }

    override fun resetCurrentId(item: T) {
        currentId = getFiledValue(item) as Long

        println("Current Select Key= $currentId")
    }
}
