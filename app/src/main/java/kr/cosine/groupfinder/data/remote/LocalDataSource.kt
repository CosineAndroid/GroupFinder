package kr.cosine.groupfinder.data.remote

import android.content.SharedPreferences

interface LocalDataSource {

    val sharedPreferences: SharedPreferences
}