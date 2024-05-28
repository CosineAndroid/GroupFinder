package kr.cosine.groupfinder.domain.repository

import kr.cosine.groupfinder.data.model.TestResponse

interface TestRepository {

    fun onTest(): TestResponse
}