package kr.cosine.krossfit.domain.mapper

import kr.cosine.krossfit.data.model.TestResponse
import kr.cosine.krossfit.domain.model.TestEntity

fun TestResponse.toEntity(): TestEntity {
    return TestEntity(test)
}