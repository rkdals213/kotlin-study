package kgp.liivm.kotlinstudy.common.kotest_acceptance

import io.restassured.RestAssured
import jakarta.annotation.PostConstruct
import kgp.liivm.kotlinstudy.common.acceptance.DatabaseCleanup
import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class KoTestAcceptanceConfiguration(
    private val databaseCleanup: DatabaseCleanup
) {

    @PostConstruct
    fun setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            databaseCleanup.afterPropertiesSet()
        }
        databaseCleanup.execute()
    }
}
