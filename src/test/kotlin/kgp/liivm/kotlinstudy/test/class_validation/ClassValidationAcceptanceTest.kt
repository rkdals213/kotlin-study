package kgp.liivm.kotlinstudy.test.class_validation

import io.kotest.matchers.shouldBe
import kgp.liivm.kotlinstudy.common.acceptance.AcceptanceTest
import kgp.liivm.kotlinstudy.common.acceptance.getResource
import kgp.liivm.kotlinstudy.common.acceptance.givenRequestSpecification
import kgp.liivm.kotlinstudy.common.acceptance.postResource
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@AcceptanceTest
class ClassValidationAcceptanceTest {

    @Test
    @DisplayName("GET 요청으로 정확한 전화번호를 보내면 성공한다")
    fun test01() {
        val response = getResource(
            request = givenRequestSpecification(),
            url = "/class-validation",
            params = mapOf("phoneNumber" to CORRECT_PHONE_NUMBER)
        )

        response.statusCode() shouldBe 200
    }

    @Test
    @DisplayName("GET 요청으로 부정확한 전화번호를 보내면 성공한다")
    fun test02() {
        val response = getResource(
            request = givenRequestSpecification(),
            url = "/class-validation",
            params = mapOf("phoneNumber" to INCORRECT_PHONE_NUMBER)
        )

        response.statusCode() shouldBe 500
    }

    @Test
    @DisplayName("POST 요청으로 정확한 전화번호를 보내면 성공한다")
    fun test03() {
        val body = """{
                "phoneNumber" : "$CORRECT_PHONE_NUMBER"
            }
        """.trimIndent()

        val response = postResource(
            request = givenRequestSpecification(),
            url = "/class-validation",
            body = body
        )

        response.statusCode() shouldBe 200
    }

    @Test
    @DisplayName("POST 요청으로 부정확한 전화번호를 보내면 성공한다")
    fun test04() {
        val body = """{
                "phoneNumber" : "$INCORRECT_PHONE_NUMBER"
            }
        """.trimIndent()

        val response = postResource(
            request = givenRequestSpecification(),
            url = "/class-validation",
            body = body
        )

        response.statusCode() shouldBe 400
    }

    companion object {
        private const val CORRECT_PHONE_NUMBER = "010-1234-5678"
        private const val INCORRECT_PHONE_NUMBER = "010-1234-56789"
    }
}
