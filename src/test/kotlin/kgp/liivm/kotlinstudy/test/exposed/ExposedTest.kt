package kgp.liivm.kotlinstudy.test.exposed

import io.kotest.core.spec.style.FunSpec
import kgp.liivm.kotlinstudy.common.acceptance.AcceptanceTest
import kgp.liivm.kotlinstudy.exposed.MemberExposedEntity
import kgp.liivm.kotlinstudy.exposed.TeamExposedEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

@AcceptanceTest
class ExposedTest : FunSpec({

    context("exposed 생성 테스트") {
        val database = Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

        transaction(database) {
            SchemaUtils.create(TeamExposedEntity, MemberExposedEntity)
            addLogger(StdOutSqlLogger)

            TeamExposedEntity.insert {
                it[name] = "My Team"
                it[createdDatetime] = LocalDateTime.now()
            }

            commit()
        }

        test("엔티티 생성") {

        }
    }
})
