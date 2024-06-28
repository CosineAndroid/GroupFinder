package kr.cosine.groupfinder

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import kr.cosine.groupfinder.data.manager.LocalAccountManager
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.presentation.view.account.AccountActivity

@HiltAndroidApp
class GroupFinderApplication : Application() {

    private var isForeground = false
    private lateinit var currentActivity: Context

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (activity !is AccountActivity && LocalAccountRegistry.findUniqueId() == null) {
                    LocalAccountManager(activity).findUniqueId()
                        .let(LocalAccountRegistry::setUniqueId)
                }
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {
                isForeground = true
                currentActivity = activity
                Log.d("lifecycle", "onActivityResumed: $isForeground, $currentActivity")
            }

            override fun onActivityPaused(activity: Activity) {
                isForeground = false
                Log.d("lifecycle", "onActivityResumed: $isForeground")
            }

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })

    }

    fun isForeground(): Boolean {
        return isForeground
    }

    fun getCurrentActivity(): Context {
        return currentActivity
    }
}
