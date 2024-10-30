package kgp.liivm.kotlinstudy.test_env_test

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(catalog = "kotlin_study", name = "test_entity")
class TestEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val data: String
)

interface TestEntityRepository : JpaRepository<TestEntity, Long> {}
