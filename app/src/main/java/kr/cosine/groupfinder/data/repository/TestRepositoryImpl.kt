package kr.cosine.groupfinder.data.repository

import android.util.Log
import kr.cosine.groupfinder.data.model.TestResponse
import kr.cosine.groupfinder.data.remote.TestDataSource
import kr.cosine.groupfinder.domain.repository.TestRepository
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testDataSource: TestDataSource
) : TestRepository {

    override fun onTest(): TestResponse {
        Log.d("GroupFinderTest", "Test Method invoke")
        return testDataSource.onTest()
    }
}