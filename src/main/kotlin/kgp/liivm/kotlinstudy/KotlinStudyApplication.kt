package kgp.liivm.kotlinstudy

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(
    exclude = [ExposedAutoConfiguration::class]
)
@EnableJpaRepositories(transactionManagerRef = "jpaTransactionManager")  // 이러면 spring data jpa repository 에서 jpaTransactionManager 를 사용함
class KotlinStudyApplication

fun main(args: Array<String>) {
    runApplication<KotlinStudyApplication>(*args)
}
