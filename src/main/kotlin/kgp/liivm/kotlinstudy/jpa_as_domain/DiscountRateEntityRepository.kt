package kgp.liivm.kotlinstudy.jpa_as_domain

import org.springframework.data.jpa.repository.JpaRepository

interface DiscountRateEntityRepository: JpaRepository<DiscountRateEntity, Long> {
}
