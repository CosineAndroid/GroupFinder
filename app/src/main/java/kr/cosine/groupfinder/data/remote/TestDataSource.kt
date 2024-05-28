package kr.cosine.groupfinder.data.remote

import kr.cosine.groupfinder.data.model.TestResponse

interface TestDataSource {

    // Get 어노테이션
    fun onTest(): TestResponse
}