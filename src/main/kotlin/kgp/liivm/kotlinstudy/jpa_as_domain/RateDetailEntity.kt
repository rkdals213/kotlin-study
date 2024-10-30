package kgp.liivm.kotlinstudy.jpa_as_domain

import jakarta.persistence.*

@Entity
@Table(catalog = "kotlin_study", name = "rate_detail_entity")
class RateDetailEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val contractStatus: String,
    val amount: Int,

    @ManyToOne
    val rateEntity: RateEntity
)
