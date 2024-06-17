package kr.cosine.groupfinder.presentation.view.common

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kr.cosine.groupfinder.presentation.view.common.model.LoginSessionViewModel
import kr.cosine.groupfinder.presentation.view.dialog.Dialog

abstract class GroupFinderActivity : AppCompatActivity() {

    private val loginSessionViewModel by viewModels<LoginSessionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginSessionViewModel.removeLoginSession()
    }

    override fun onResume() {
        super.onResume()
        loginSessionViewModel.registerLoginSessionListener(this) {
            showDuplicationLoginDialog()
        }
    }

    private fun showDuplicationLoginDialog() {
        Dialog.dismiss(this) // 안됨, 고쳐야 함
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