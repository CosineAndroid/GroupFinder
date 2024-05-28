package kr.cosine.krossfit.domain.repository

import kr.cosine.krossfit.data.model.TestResponse

interface TestRepository {

    fun onTest(): TestResponse
}