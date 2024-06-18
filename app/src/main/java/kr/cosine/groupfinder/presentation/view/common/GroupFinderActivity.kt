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

    private var useLoginSession = false

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
    }

    private fun showDuplicationLoginDialog() {
        supportFragmentManager.fragments.forEach {
            (it as? DialogFragment)?.dismiss()
        }
        Dialog(
            message = "다른 위치에서 로그인하였습니다.",
            cancelButtonVisibility = View.GONE
        ) {
            finishAffinity()
        }.apply {
            isCancelable = false
        }.show(supportFragmentManager, Dialog.TAG)
    }
}