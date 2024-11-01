package kgp.liivm.kotlinstudy.test.test_env_test

import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kgp.liivm.kotlinstudy.common.acceptance.getResource
import kgp.liivm.kotlinstudy.common.acceptance.givenRequestSpecification
import kgp.liivm.kotlinstudy.common.acceptance.postResource
import kgp.liivm.kotlinstudy.common.acceptance.AcceptanceTest

@AcceptanceTest
class TestEnvTest : FunSpec({

    context("kotest와 restassured를 사용한 테스트") {
        test("api 호출에 성공한다") {
            val body = """
                {
                    "data" : "this is kotest + restassured"
                }
            """.trimIndent()

            postResource(
                request = givenRequestSpecification(),
                url = "/kotest-restassured",
                body = body
            )
        }
    }

    context("데이터 1") {
        val body = """
                {
                    "data" : "data 1"
                }
            """.trimIndent()

        test("1만 리턴된다") {
            postResource(
                request = givenRequestSpecification(),
                url = "/kotest-restassured/insert",
                body = body
            )

            val response = getResource(
                request = givenRequestSpecification(),
                url = "/kotest-restassured/get",
            )

            response.jsonPath().getInt("body[0].id") shouldBe 1
            response.jsonPath().getString("body[0].data") shouldBe "data 1"
        }

    }

    context("데이터 2") {
        val body = """
                {
                    "data" : "data 2"
                }
            """.trimIndent()

        test("2만 리턴된다") {
            postResource(
                request = givenRequestSpecification(),
                url = "/kotest-restassured/insert",
                body = body
            )

            val response = getResource(
                request = givenRequestSpecification(),
                url = "/kotest-restassured/get",
            )

            response.jsonPath().getInt("body[0].id") shouldBe 1
            response.jsonPath().getString("body[0].data") shouldBe "data 2"

        }
    }

    extension(SpringExtension)

})
