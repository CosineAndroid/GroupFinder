package kr.cosine.groupfinder.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.cosine.groupfinder.data.remote.FirebaseDataSource

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    fun provideFirebaseDataSource(): FirebaseDataSource {
        return object : FirebaseDataSource {
            override val firestore = Firebase.firestore
        }
    }
}