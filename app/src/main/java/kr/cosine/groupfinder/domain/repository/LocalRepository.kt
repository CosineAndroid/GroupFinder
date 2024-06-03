package kr.cosine.groupfinder.domain.repository

import java.util.UUID

interface LocalRepository {

    fun getUniqueId(): UUID

    fun getSharedPreference()
}