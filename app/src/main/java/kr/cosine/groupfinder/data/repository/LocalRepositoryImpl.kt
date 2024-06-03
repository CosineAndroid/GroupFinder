package kr.cosine.groupfinder.data.repository

import kr.cosine.groupfinder.data.remote.LocalDataSource
import kr.cosine.groupfinder.domain.repository.LocalRepository
import java.util.UUID
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : LocalRepository {

    private val sharedPreferences get() = localDataSource.sharedPreferences

    override fun getUniqueId(): UUID {
        sharedPreferences
    }
}