package kr.cosine.groupfinder.presentation.view.account.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.presentation.view.account.component.BaseTextField
import kr.cosine.groupfinder.presentation.view.account.component.BaseButton
import kr.cosine.groupfinder.presentation.view.account.extension.showToast
import kr.cosine.groupfinder.presentation.view.account.intent.IntentKey
import kr.cosine.groupfinder.presentation.view.account.login.model.LoginViewModel
import kr.cosine.groupfinder.presentation.view.account.register.RegisterActivity
import kr.cosine.groupfinder.presentation.view.account.ui.CustomColor

class LoginActivity : ComponentActivity() {

    private val loginViewModel by viewModels<LoginViewModel>()

    private lateinit var accountResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerAccountResultLauncher()
        setContent {
            Login(
                onLoginButtonClick = this::startRegisterActivity
            )
        }
    }

    private fun registerAccountResultLauncher() {
        accountResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != RESULT_OK) return@registerForActivityResult
            val intent = result.data ?: return@registerForActivityResult
            val id = intent.getStringExtra(IntentKey.ID) ?: EMPTY
            val password = intent.getStringExtra(IntentKey.PASSWORD) ?: EMPTY
            loginViewModel.setIdAndPassword(id, password)
        }
    }

    private fun startRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        accountResultLauncher.launch(intent)
    }

    private companion object {
        const val EMPTY = ""
    }
}

@Composable
private fun Login(
    loginViewModel: LoginViewModel = viewModel(),
    onLoginButtonClick: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
        BaseTextField(
            text = uiState.id,
            hint = stringResource(R.string.login_id_hint),
            onValueChange = loginViewModel::setId
        )
        BaseTextField(
            text = uiState.password,
            hint = stringResource(R.string.login_password_hint),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = loginViewModel::setPassword
        )
        BaseButton(
            text = stringResource(R.string.login),
        ) {
            context.showToast("로그인")
        }
        BaseButton(
            text = stringResource(R.string.register),
            containerColor = CustomColor.RegisterButtonBackground,
            onClick = onLoginButtonClick
        )
    }
}