package kr.cosine.groupfinder.presentation.view.common

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.common.model.LoginSessionViewModel
import kr.cosine.groupfinder.presentation.view.dialog.Dialog

abstract class GroupFinderActivity : AppCompatActivity() {

    private val loginSessionViewModel by viewModels<LoginSessionViewModel>()
    //private val broadcastListViewModel by viewModels<BroadcastListViewModel>()

    private var useLoginSession = false

    /*private val ringtoneUri by lazy {
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }
    private val ringtone by lazy {
        RingtoneManager.getRingtone(this, ringtoneUri)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useLoginSession = intent.getBooleanExtra(IntentKey.USE_LOGIN_SESSION, true)
        if (useLoginSession) {
            loginSessionViewModel.removeLoginSession()
        }
    }

    override fun onResume() {
        super.onResume()
        if (useLoginSession) {
            loginSessionViewModel.registerLoginSessionListener(this) {
                showDuplicationLoginDialog()
            }
        }
        /*broadcastListViewModel.registerBroadcastListener(this) {
            showBroadcastNotification()
        }*/
    }

    private fun showDuplicationLoginDialog() {
        dismissAllDialog()
        Dialog(
            message = ANOTHER_LOCATION_LOGIN_MESSAGE,
            cancelButtonVisibility = View.GONE
        ) {
            finishAffinity()
        }.apply {
            isCancelable = false
        }.show(supportFragmentManager, Dialog.TAG)
    }

    /*private fun showBroadcastNotification() {
        ringtone.play()
        Dialog(
            title = NEW_BROADCAST_TITLE,
            message = NEW_BROADCAST_MESSAGE,
            cancelButtonVisibility = View.GONE
        ).show(supportFragmentManager, Dialog.TAG)
    }*/

    private fun dismissAllDialog() {
        supportFragmentManager.fragments.forEach {
            (it as? DialogFragment)?.dismiss()
        }
    }

    private companion object {
        const val ANOTHER_LOCATION_LOGIN_MESSAGE = "다른 위치에서 로그인하였습니다."
        /*const val NEW_BROADCAST_TITLE = "공지사항"
        const val NEW_BROADCAST_MESSAGE = "새로운 공지가 등록되었습니다."*/
    }
}