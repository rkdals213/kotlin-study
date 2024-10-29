package kgp.liivm.kotlinstudy.common.kotest_acceptance

import io.restassured.RestAssured
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
        val transactionTemplate = testContext.applicationContext.getBean(TransactionTemplate::class.java)
        val jdbcTemplate = testContext.applicationContext.getBean(JdbcTemplate::class.java)

        val tableNames = getTableNames(jdbcTemplate)
        println("table names$tableNames")

        transactionTemplate.execute(object : TransactionCallbackWithoutResult() {
            override fun doInTransactionWithoutResult(status: TransactionStatus) {
                jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE;")
                for (tableName in tableNames) {
                    jdbcTemplate.execute("TRUNCATE TABLE $tableName RESTART IDENTITY;")
                    jdbcTemplate.execute("ALTER TABLE $tableName ALTER COLUMN ID RESTART WITH 1;")
                }
                jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE;")
            }
        })
    }

    private fun getTableNames(jdbcTemplate: JdbcTemplate): List<String> {
        return jdbcTemplate.queryForList(
            "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
            String::class.java
        )
    }
}
