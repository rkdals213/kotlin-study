package kgp.liivm.kotlinstudy.jpa_as_domain

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rate")
class RateController(
    private val planRateEntityRepository: PlanRateEntityRepository,
    private val discountRateEntityRepository: DiscountRateEntityRepository,
    private val rateDetailEntityRepository: RateDetailEntityRepository
) {

    @PostMapping("/plan")
    @Transactional
    fun test01() {
        val planRateEntity = PlanRateEntity(
            id = 0,
            name = "요금제 과금",
            code = "RI00001",
            attr = PlanRateAttribute(
                attr01 = "attr01",
                attr02 = "attr02",
                attr03 = "attr03",
                attr04 = "attr04",
            )
        )

        val rateDetailEntities = listOf(
            RateDetailEntity(0L, "20", 10000, planRateEntity),
            RateDetailEntity(0L, "30", 3000, planRateEntity),
            RateDetailEntity(0L, "40", 3000, planRateEntity)
        )

        planRateEntity.rateDetails.addAll(rateDetailEntities)

        planRateEntityRepository.save(planRateEntity)
        rateDetailEntityRepository.saveAll(rateDetailEntities)
    }

    @PostMapping("/discount")
    @Transactional
    fun test02() {
        val discountRateEntity = DiscountRateEntity(
            id = 0,
            name = "요금제 과금",
            code = "RI00001",
            attr = DiscountRateAttribute(
                attr05 = "attr05",
                attr06 = "attr06",
                attr07 = "attr07",
                attr08 = "attr08",
                attr09 = "attr09",
            )
        )

        val rateDetailEntities = mutableListOf(
            RateDetailEntity(0L, "20", -1000, discountRateEntity)
        )

        discountRateEntity.rateDetails.addAll(rateDetailEntities)

        discountRateEntityRepository.save(discountRateEntity)
        rateDetailEntityRepository.saveAll(rateDetailEntities)
    }

}
