package kgp.liivm.batchstudy.common.entity

import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManagerFactory
import kgp.liivm.batchstudy.common.querydsl_reader.QuerydslNoOffsetPagingItemReader
import kgp.liivm.batchstudy.common.querydsl_reader.options.QuerydslNoOffsetOptions
import java.util.function.Function

class QuerydslNoOffsetPagingItemReaderBuilder<T> {
    lateinit var enf: EntityManagerFactory
    lateinit var options: QuerydslNoOffsetOptions<T>
    var pageSize: Int = 0
    lateinit var queryFunction: Function<JPAQueryFactory, JPAQuery<T>>

    fun build(): QuerydslNoOffsetPagingItemReader<T> {
        return QuerydslNoOffsetPagingItemReader(
            enf,
            pageSize,
            options,
            queryFunction
        )
    }
}

fun <T> QuerydslNoOffsetPagingItemReaderBuilder(block: QuerydslNoOffsetPagingItemReaderBuilder<T>. () -> Unit): QuerydslNoOffsetPagingItemReaderBuilder<T> {
    val builder = QuerydslNoOffsetPagingItemReaderBuilder<T>()
    builder.block()
    return builder
}
