package kgp.liivm.kotlinstudy.common.acceptance

import com.google.common.base.CaseFormat
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Table
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@Service
@ActiveProfiles("test")
class DatabaseCleanup(
    @PersistenceContext
    private val entityManager: EntityManager
) : InitializingBean {
    private var tableNames: List<String>? = null

    override fun afterPropertiesSet() {
        tableNames = entityManager.metamodel.entities
            .filter {
                it.javaType.getAnnotation(Entity::class.java) != null || it.javaType.getAnnotation(Table::class.java) != null
            }
            .map {
                var name = ""

                if (it.javaType.getAnnotation(Table::class.java) != null) {
                    val annotation = it.javaType.getAnnotation(Table::class.java)
                    name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, annotation.name)
                } else if (it.javaType.getAnnotation(Entity::class.java) != null) {
                    val annotation = it.javaType.getAnnotation(Entity::class.java)
                    name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, annotation.name)
                }

                if (name.isEmpty()) {
                    name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, it.name)
                }
                name
            }
            .toList()

        println(tableNames)
    }

    @Transactional
    fun execute() {
        entityManager.flush()
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
        for (tableName in tableNames!!) {
            entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
            entityManager.createNativeQuery("ALTER TABLE $tableName ALTER COLUMN ID RESTART WITH 1").executeUpdate()
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
    }
}
