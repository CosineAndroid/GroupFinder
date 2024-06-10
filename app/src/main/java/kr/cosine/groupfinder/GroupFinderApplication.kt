package kr.cosine.groupfinder

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GroupFinderApplication : Application() {
    private var _isForeground = false
    private lateinit var currentActivity: Context

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
                _isForeground = true
                currentActivity = activity
                Log.d("lifecycle", "onActivityResumed: ${_isForeground}, ${currentActivity}")
            }

            override fun onActivityPaused(activity: Activity) {
                _isForeground = false
                Log.d("lifecycle", "onActivityResumed: ${_isForeground}")
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

        })

    }
    fun isForeground(): Boolean {
        return _isForeground
    }

    fun getCurrentActivity(): Context {
        return currentActivity
    }
}
