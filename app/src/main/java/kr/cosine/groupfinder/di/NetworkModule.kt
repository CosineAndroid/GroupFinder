package kr.cosine.groupfinder.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.cosine.groupfinder.data.model.TestResponse
import kr.cosine.groupfinder.data.remote.TestDataSource

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideTestDataSource(): TestDataSource {
        return object : TestDataSource {
            override fun onTest(): TestResponse {
                return TestResponse("테스트")
            }
        }
    }
}