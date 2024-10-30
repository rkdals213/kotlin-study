package kgp.liivm.kotlinstudy.jpa_as_domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@DiscriminatorValue("PlanRate")
@Table(catalog = "kotlin_study", name = "rate_entity")
class PlanRateEntity(
    id: Long = 0L,

    code: String,
    name: String,
    rateDetails: MutableList<RateDetailEntity> = mutableListOf(),

    val attr: PlanRateAttribute
) : RateEntity(id, code, name, rateDetails)


@Embeddable
class PlanRateAttribute(
    val attr01: String,
    val attr02: String,
    val attr03: String,
    val attr04: String,
)
