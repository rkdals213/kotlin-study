package kgp.liivm.batchstudy.common.querydsl_reader.expression

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.core.types.dsl.StringPath

enum class WhereExpression(
    private val string: WhereStringFunction,
    private val long: WhereNumberFunction<Long>,
    private val int: WhereNumberFunction<Int>
) {
    GT(
        string = WhereStringFunction { id, page, currentId ->
            if (page == 0) id.goe(currentId) else id.gt(currentId)
        },
        long = WhereNumberFunction { id, page, currentId ->
            if (page == 0) id.goe(currentId) else id.gt(currentId)
        },
        int = WhereNumberFunction { id, page, currentId ->
            if (page == 0) id.goe(currentId) else id.gt(currentId)
        }
    ),
    LT(
        string = WhereStringFunction { id, page, currentId ->
            if (page == 0) id.loe(currentId) else id.lt(currentId)
        },
        long = WhereNumberFunction { id, page, currentId ->
            if (page == 0) id.loe(currentId) else id.lt(currentId)
        },
        int = WhereNumberFunction { id, page, currentId ->
            if (page == 0) id.goe(currentId) else id.gt(currentId)
        }
    );

    fun expression(id: StringPath, page: Int, currentId: String): BooleanExpression {
        return string.apply(id, page, currentId)
    }

    fun expression(id: NumberPath<Long>, page: Int, currentId: Long): BooleanExpression {
        return long.apply(id, page, currentId)
    }

    fun expression(id: NumberPath<Int>, page: Int, currentId: Int): BooleanExpression {
        return int.apply(id, page, currentId)
    }
}

sealed interface WhereExpression2 {
    class Greater<N> : WhereExpression2 where N : Number, N : Comparable<*> {
        override val string: WhereStringFunction = WhereStringFunction { id, page, currentId ->
            if (page == 0) id.goe(currentId) else id.gt(currentId)
        }
        override val number: WhereNumberFunction<N> = WhereNumberFunction { id, page, currentId ->
            if (page == 0) id.goe(currentId) else id.gt(currentId)
        }
    }

    val string: WhereStringFunction
    val number: WhereNumberFunction<*>
}
