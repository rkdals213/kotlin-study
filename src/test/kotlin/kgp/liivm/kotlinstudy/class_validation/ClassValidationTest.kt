package kgp.liivm.kotlinstudy.class_validation

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class ClassValidationTest : BehaviorSpec({

    Given("정확한 전화번호로") {
        val correctPhoneNumber = "010-1234-5678"

        When("전화번호 객체를 만들면") {
            val phoneNumber = PhoneNumber(correctPhoneNumber)

            Then("성공한다") {
                phoneNumber.number shouldBe correctPhoneNumber
                println(phoneNumber)
            }
        }
    }

    Given("부정확한 전화번호로") {
        val incorrectPhoneNumber = "0101234-5678"

        When("전화번호 객체를 만들면") {
            val exception = shouldThrow<IllegalArgumentException> {
                PhoneNumber(incorrectPhoneNumber)
            }

            Then("실패한다") {
                exception.message shouldContain "Invalid phone number"
                println(exception.message)
            }
        }
    }
})
