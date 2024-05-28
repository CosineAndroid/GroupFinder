package kr.cosine.groupfinder.domain.mapper

import kr.cosine.groupfinder.data.model.TestResponse
import kr.cosine.groupfinder.domain.model.TestEntity

fun TestResponse.toEntity(): TestEntity {
    return TestEntity(test)
}