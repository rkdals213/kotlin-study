package kgp.liivm.kotlinstudy.common.acceptance

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestExecutionListeners

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@TestExecutionListeners(
    value = [AcceptanceTestExecutionListener::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@ActiveProfiles("test")
annotation class AcceptanceTest



@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
)
@TestExecutionListeners(
    value = [ExposedAcceptanceTestExecutionListener::class],
    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS
)
@ActiveProfiles("test")
annotation class ExposedAcceptanceTest
