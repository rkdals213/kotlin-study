package kgp.liivm.kotlinstudy.function_extension

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import kgp.liivm.kotlinstudy.function_extension.StringUtils.maskingPhoneNumber

class FunctionExtensionTest : BehaviorSpec({
    Given("전화번호를") {
        val phoneNumber = "010-1234-5678"

        When("마스킹 처리하면") {
            val maskingPhoneNumber = phoneNumber.maskingPhoneNumber()

            Then("가운데 숫자가 마스킹된다") {
                maskingPhoneNumber shouldBe "010-****-5678"
            }
        }
    }
})
