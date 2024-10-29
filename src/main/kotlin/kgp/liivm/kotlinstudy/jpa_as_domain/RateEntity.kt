package kgp.liivm.kotlinstudy.jpa_as_domain

import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
abstract class RateEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    val code: String,
    val name: String,

    @OneToMany(mappedBy = "rateEntity")
    val rateDetails: MutableList<RateDetailEntity>
)