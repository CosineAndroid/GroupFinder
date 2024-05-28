package kr.cosine.krossfit.data.remote

import kr.cosine.krossfit.data.model.TestResponse

interface TestDataSource {

    // Get 어노테이션
    fun onTest(): TestResponse
}