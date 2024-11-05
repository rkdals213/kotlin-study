package kgp.liivm.batchstudy

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ExposedAutoConfiguration::class])
class BatchStudyApplication

fun main(args: Array<String>) {
    runApplication<BatchStudyApplication>(*args)
}
