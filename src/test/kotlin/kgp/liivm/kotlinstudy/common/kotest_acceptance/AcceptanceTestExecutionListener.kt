package kgp.liivm.kotlinstudy.common.kotest_acceptance

import io.restassured.RestAssured
import jakarta.persistence.EntityManager
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.TestContext
import org.springframework.test.context.support.AbstractTestExecutionListener
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate

class AcceptanceTestExecutionListener : AbstractTestExecutionListener() {

    override fun beforeTestClass(testContext: TestContext) {
        val serverPort = testContext.applicationContext.environment.getProperty("local.server.port", Int::class.java) ?: throw IllegalStateException("localServerPort cannot be null")
        RestAssured.port = serverPort
    }

    override fun beforeTestMethod(testContext: TestContext) {
        clearDatabaseWithJDBC(testContext)
//        clearDatabaseWithJPA(testContext)
    }

    private fun clearDatabaseWithJPA(testContext: TestContext) {
        val transactionTemplate = testContext.applicationContext.getBean(TransactionTemplate::class.java)
        val entityManager = testContext.applicationContext.getBean(EntityManager::class.java)

        transactionTemplate.execute(object : TransactionCallbackWithoutResult() {
            override fun doInTransactionWithoutResult(status: TransactionStatus) {
                val tableNames = getTableNames(entityManager)

                entityManager.flush()
                entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate()
                for (tableName in tableNames) {
                    entityManager.createNativeQuery("TRUNCATE TABLE $tableName").executeUpdate()
                    entityManager.createNativeQuery("ALTER TABLE $tableName ALTER COLUMN ID RESTART WITH 1").executeUpdate()
                }
                entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate()
            }
        })
    }

    private fun clearDatabaseWithJDBC(testContext: TestContext) {
        val transactionTemplate = testContext.applicationContext.getBean(TransactionTemplate::class.java)
        val jdbcTemplate = testContext.applicationContext.getBean(JdbcTemplate::class.java)

        val tableNames = getTableNames(jdbcTemplate)

        transactionTemplate.execute(object : TransactionCallbackWithoutResult() {
            override fun doInTransactionWithoutResult(status: TransactionStatus) {
                jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE;")
                for (tableName in tableNames) {
                    jdbcTemplate.execute("TRUNCATE TABLE $tableName;")
                    jdbcTemplate.execute("ALTER TABLE $tableName ALTER COLUMN ID RESTART WITH 1;")
                }
                jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE;")
            }
        })
    }

    private fun getTableNames(entityManager: EntityManager): List<String> {
        return entityManager.createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'")
            .resultList
            .map { it.toString() }
    }

    private fun getTableNames(jdbcTemplate: JdbcTemplate): List<String> {
        return jdbcTemplate.queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String::class.java)
    }
}
