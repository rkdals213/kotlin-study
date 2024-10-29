package kgp.liivm.kotlinstudy.jpa_as_domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@DiscriminatorValue("DiscountRate")
class DiscountRateEntity(
    id: Long = 0L,

    code: String,
    name: String,
    rateDetails: MutableList<RateDetailEntity> = mutableListOf(),

    val attr: DiscountRateAttribute
) : RateEntity(id, code, name, rateDetails)

@Embeddable
class DiscountRateAttribute(
    val attr05: String,
    val attr06: String,
    val attr07: String,
    val attr08: String,
    val attr09: String
)
