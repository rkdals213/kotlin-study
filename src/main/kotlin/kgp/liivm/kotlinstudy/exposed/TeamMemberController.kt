package kgp.liivm.kotlinstudy.exposed

import kgp.liivm.kotlinstudy.common.config.Tx
import kgp.liivm.kotlinstudy.test_env_test.TestEntity
import kgp.liivm.kotlinstudy.test_env_test.TestEntityRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/teamMembers")
class TeamMemberController(
    private val testEntityRepository: TestEntityRepository
) {

    @PostMapping
    fun test01() {
        Tx.exposedTxm {
            val team = TeamExposedEntity.insert {
                it[name] = "my team"
                it[createdDatetime] = LocalDateTime.now()
            }

            MemberExposedEntity.insert {
                it[name] = "kang"
                it[age] = 10
                it[teamId] = team[TeamExposedEntity.id]
            }
        }
    }

    @GetMapping
    fun test02(): List<TeamMemberDomain> {
        return Tx.exposedTxm {
            val memberResults = (TeamExposedEntity innerJoin MemberExposedEntity)
                .selectAll()
                .where { MemberExposedEntity.age lessEq 10 }

            val memberDomains = memberResults.map {
                MemberDomain(
                    memberId = it[MemberExposedEntity.id],
                    name = it[MemberExposedEntity.name],
                    age = it[MemberExposedEntity.age]
                )
            }

            TeamExposedEntity.selectAll()
                .where { TeamExposedEntity.id inList memberResults.map { it[TeamExposedEntity.id] } }
                .map {
                    TeamMemberDomain(
                        teamId = it[TeamExposedEntity.id],
                        teamName = it[TeamExposedEntity.name],
                        createdDateTime = it[TeamExposedEntity.createdDatetime],
                        members = memberDomains
                    )
                }
        }
    }

    @GetMapping("/mix")
    fun test03(): Pair<List<TeamMemberDomain>, TestEntity> {
        val teamMemberDomains = Tx.exposedTxm {
            val memberResults = (TeamExposedEntity innerJoin MemberExposedEntity)
                .selectAll()
                .where { MemberExposedEntity.age lessEq 10 }

            val memberDomains = memberResults.map {
                MemberDomain(
                    memberId = it[MemberExposedEntity.id],
                    name = it[MemberExposedEntity.name],
                    age = it[MemberExposedEntity.age]
                )
            }

            TeamExposedEntity.selectAll()
                .where { TeamExposedEntity.id inList memberResults.map { it[TeamExposedEntity.id] } }
                .map {
                    TeamMemberDomain(
                        teamId = it[TeamExposedEntity.id],
                        teamName = it[TeamExposedEntity.name],
                        createdDateTime = it[TeamExposedEntity.createdDatetime],
                        members = memberDomains
                    )
                }
        }

        val testEntity = Tx.jpaTxm {
            testEntityRepository.save(TestEntity(data = "testData"))
        }

        return teamMemberDomains to testEntity
    }
}
