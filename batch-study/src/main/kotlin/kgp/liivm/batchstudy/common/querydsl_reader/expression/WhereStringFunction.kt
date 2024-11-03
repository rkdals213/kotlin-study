package kgp.liivm.batchstudy.common.querydsl_reader.expression

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath

fun interface WhereStringFunction {
    fun apply(id: StringPath, page: Int, currentId: String): BooleanExpression
}

fun interface WhereNumberFunction<N> where N : Number, N : Comparable<*> {
    fun apply(id: NumberPath<N>, page: Int, currentId: N): BooleanExpression
}
