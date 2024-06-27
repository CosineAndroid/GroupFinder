package kr.cosine.groupfinder.presentation.view.account.register

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import kr.cosine.groupfinder.presentation.view.account.AccountActivity
import kr.cosine.groupfinder.presentation.view.account.register.screen.RegisterScreen

@AndroidEntryPoint
class RegisterActivity : AccountActivity() {

    @Composable
    override fun Screen() {
        RegisterScreen()
    }
}