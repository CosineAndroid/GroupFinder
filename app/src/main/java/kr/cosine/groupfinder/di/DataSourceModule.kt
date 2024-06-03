package kr.cosine.groupfinder.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.cosine.groupfinder.data.model.TestResponse
import kr.cosine.groupfinder.data.remote.FirebaseDataSource
import kr.cosine.groupfinder.data.remote.TestDataSource

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    fun provideTestDataSource(): TestDataSource {
        return object : TestDataSource {
            override fun onTest(): TestResponse {
                return TestResponse("테스트")
            }
        }
    }

    @Provides
    fun provideFirebaseDataSource(): FirebaseDataSource {
        return object : FirebaseDataSource {
            override val firestore = Firebase.firestore
        }
    }
}