package kr.cosine.groupfinder.presentation.view.account.login

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.presentation.view.account.AccountActivity
import kr.cosine.groupfinder.presentation.view.account.login.screen.LoginScreen

@AndroidEntryPoint
class LoginActivity : AccountActivity() {

    @Composable
    override fun Screen() {
        LoginScreen()
    }
}