package kgp.liivm.kotlinstudy

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = [ExposedAutoConfiguration::class]
)
class KotlinStudyApplication

fun main(args: Array<String>) {
    runApplication<KotlinStudyApplication>(*args)
}
