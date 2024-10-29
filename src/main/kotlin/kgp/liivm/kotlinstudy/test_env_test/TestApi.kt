package kgp.liivm.kotlinstudy.test_env_test

import kgp.liivm.kotlinstudy.api_response.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/kotest-restassured")
class TestApi(
    private val testEntityRepository: TestEntityRepository
) {

    @PostMapping
    fun test01(@RequestBody testData: TestData): TestData {
        return testData
    }

    @PostMapping("/insert")
    fun test02(@RequestBody testData: TestData) {
        testEntityRepository.save(TestEntity(data = testData.data))
    }

    @GetMapping("/get")
    fun test03(): ResponseEntity<ApiResponse<MutableList<TestEntity>>> {
        return ResponseEntity.ok(
            ApiResponse(
                status = "200",
                message = "OK",
                body = testEntityRepository.findAll()
            )
        )
    }
}

data class TestData(
    val data: String
)
