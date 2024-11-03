package kgp.liivm.batchstudy.common.querydsl_reader.expression

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath

enum class Expression(
    val where: WhereExpression,
    val order: OrderExpression
) {
    ASC(WhereExpression.GT, OrderExpression.ASC),
    DESC(WhereExpression.LT, OrderExpression.DESC);

    fun where(id: StringPath, page: Int, currentId: String): BooleanExpression {
        return where.expression(id, page, currentId)
    }

    fun where(id: NumberPath<Long>, page: Int, currentId: Long): BooleanExpression {
        return where.expression(id, page, currentId)
    }

    fun where(id: NumberPath<Int>, page: Int, currentId: Int): BooleanExpression {
        return where.expression(id, page, currentId)
    }

    fun order(id: StringPath): OrderSpecifier<String> {
        return if (this == ASC) id.asc() else id.desc()
    }

    fun order(id: NumberPath<Long>): OrderSpecifier<Long> {
        return if (this == ASC) id.asc() else id.desc()
    }

    fun isAsc(): Boolean {
        return this === ASC
    }
}


