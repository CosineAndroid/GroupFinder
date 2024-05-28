package kr.cosine.krossfit.data.repository

import android.util.Log
import kr.cosine.krossfit.data.model.TestResponse
import kr.cosine.krossfit.data.remote.TestDataSource
import kr.cosine.krossfit.domain.repository.TestRepository
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testDataSource: TestDataSource
) : TestRepository {

    override fun onTest(): TestResponse {
        Log.d("KrossFitTest", "Test Method invoke")
        return testDataSource.onTest()
    }
}