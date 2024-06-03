package kr.cosine.groupfinder.presentation.view.account.login.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import kr.cosine.groupfinder.presentation.view.account.component.BaseButton
import kr.cosine.groupfinder.presentation.view.account.component.BaseTextField
import kr.cosine.groupfinder.presentation.view.account.intent.IntentKey
import kr.cosine.groupfinder.presentation.view.account.login.model.LoginViewModel
import kr.cosine.groupfinder.presentation.view.account.register.RegisterActivity
import kr.cosine.groupfinder.presentation.view.account.register.message.Message
import kr.cosine.groupfinder.presentation.view.account.ui.CustomColor

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel()
) {
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
            // context.showToast("로그인")
        }
        val context = LocalContext.current
        val registerResultLauncher = getRegisterResultLanuncher()
        BaseButton(
            text = stringResource(R.string.register),
            containerColor = CustomColor.RegisterButtonBackground
        ) {
            startRegisterActivity(context, registerResultLauncher)
        }
    }
}

@Composable
private fun getRegisterResultLanuncher(
    loginViewModel: LoginViewModel = viewModel()
): ActivityResultLauncher<Intent> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@rememberLauncherForActivityResult
        val intent = result.data ?: return@rememberLauncherForActivityResult

        val id = intent.getStringExtra(IntentKey.ID) ?: Message.EMPTY
        val password = intent.getStringExtra(IntentKey.PASSWORD) ?: Message.EMPTY

        loginViewModel.setIdAndPassword(id, password)
    }
}

private fun startRegisterActivity(
    context: Context,
    registerResultLauncher: ActivityResultLauncher<Intent>
) {
    val intent = Intent(context, RegisterActivity::class.java)
    registerResultLauncher.launch(intent)
}