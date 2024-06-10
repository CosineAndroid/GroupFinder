package kr.cosine.groupfinder.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.cosine.groupfinder.data.remote.CloudFunctionDataSource
import kr.cosine.groupfinder.data.remote.FirebaseDataSource
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    fun provideFirebaseDataSource(): FirebaseDataSource {
        return object : FirebaseDataSource {
            override val firestore = Firebase.firestore
        }
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideCloudFunctionRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://asia-northeast2-groupfinder-b2f8e.cloudfunctions.net/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideCloudFunctionDatasource(
        retrofit: Retrofit,
    ): CloudFunctionDataSource {
        return retrofit.create(CloudFunctionDataSource::class.java)
    }
}