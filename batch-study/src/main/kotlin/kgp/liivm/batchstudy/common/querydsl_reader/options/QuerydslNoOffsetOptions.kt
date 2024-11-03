package kgp.liivm.batchstudy.common.querydsl_reader.options

import com.querydsl.core.types.Path
import com.querydsl.jpa.impl.JPAQuery
import kgp.liivm.batchstudy.common.querydsl_reader.expression.Expression
import java.lang.reflect.Field

abstract class QuerydslNoOffsetOptions<T>(
    field: Path<*>,
    val expression: Expression
) {
    private var fieldName: String = field.toString().split(".").last()

    abstract fun initKeys(query: JPAQuery<T>, page: Int)
    abstract fun initFirstId(query: JPAQuery<T>)
    abstract fun initLastId(query: JPAQuery<T>)
    abstract fun createQuery(query: JPAQuery<T>, page: Int): JPAQuery<T>

    abstract fun resetCurrentId(item: T)

    protected fun getFiledValue(item: T): Any {
        try {
            val field: Field = item!!::class.java.getDeclaredField(fieldName)
            field.setAccessible(true)
            return field.get(item)
        } catch (e: NoSuchFieldException) {
            throw IllegalArgumentException("Not Found or Not Access Field")
        } catch (e: IllegalAccessException) {
            throw IllegalArgumentException("Not Found or Not Access Field")
        }
    }

    fun isGroupByQuery(query: JPAQuery<T>): Boolean {
        return isGroupByQuery(query.toString())
    }

    private fun isGroupByQuery(sql: String): Boolean {
        return sql.contains("group by")
    }
}
