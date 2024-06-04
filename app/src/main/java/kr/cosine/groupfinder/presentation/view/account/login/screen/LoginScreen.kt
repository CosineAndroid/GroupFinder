package kr.cosine.groupfinder.presentation.view.account.login.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.manager.LocalAccountManager
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.presentation.MainActivity
import kr.cosine.groupfinder.presentation.view.account.component.BaseButton
import kr.cosine.groupfinder.presentation.view.account.component.BaseText
import kr.cosine.groupfinder.presentation.view.account.component.BaseTextField
import kr.cosine.groupfinder.presentation.view.account.component.LoadingScreen
import kr.cosine.groupfinder.presentation.view.account.component.Space
import kr.cosine.groupfinder.presentation.view.account.intent.IntentKey
import kr.cosine.groupfinder.presentation.view.account.login.event.LoginEvent
import kr.cosine.groupfinder.presentation.view.account.login.model.LoginViewModel
import kr.cosine.groupfinder.presentation.view.account.model.LoadingViewModel
import kr.cosine.groupfinder.presentation.view.account.register.RegisterActivity
import kr.cosine.groupfinder.presentation.view.account.ui.CustomColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
) {
    val activity = LocalContext.current as ComponentActivity
    val localAccountManager = LocalAccountManager(activity)
    LoadingScreen()
    val uniqueId = localAccountManager.findUniqueId()
    if (localAccountManager.isAutoLogin() && uniqueId != null) {
        loadingViewModel.show()
        LaunchedEffect(
            key1 = Unit
        ) {
            loginViewModel.loginByUniqueId(uniqueId)
            return@LaunchedEffect
        }
    }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(
        key1 = Unit
    ) {
        onLoginEvent(
            localAccountManager,
            activity,
            lifecycle,
            snackbarHostState,
            loginViewModel,
            loadingViewModel
        )
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
            BaseTextField(
                text = uiState.id,
                hint = stringResource(R.string.login_id_hint),
                onValueChange = loginViewModel::setId
            )
            HeightSpace()
            BaseTextField(
                text = uiState.password,
                hint = stringResource(R.string.login_password_hint),
                visualTransformation = PasswordVisualTransformation(),
                onValueChange = loginViewModel::setPassword
            )
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        start = 25.dp
                    )
            ) {
                var isAutoLogin by rememberSaveable { mutableStateOf(localAccountManager.isAutoLogin()) }
                Checkbox(
                    checked = isAutoLogin,
                    onCheckedChange = {
                        localAccountManager.setAutoLogin(it)
                        isAutoLogin = it
                    },
                    modifier = Modifier
                        .size(40.dp)
                )
                BaseText(
                    text = "자동 로그인",
                    fontSize = 15.sp
                )
            }
            BaseButton(
                text = stringResource(R.string.login),
            ) {
                loadingViewModel.show()
                loginViewModel.loginByInput()
            }
            HeightSpace()
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
}

@Composable
private fun HeightSpace() {
    Space(
        height = 10.dp
    )
}

private suspend fun onLoginEvent(
    localAccountManager: LocalAccountManager,
    activity: ComponentActivity,
    lifecycle: Lifecycle,
    snackbarHostState: SnackbarHostState,
    loginViewModel: LoginViewModel,
    loadingViewModel: LoadingViewModel
) {
    loginViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
        when (event) {
            is LoginEvent.Success -> {
                val uniqueId = event.accountEntity.uniqueId
                LocalAccountRegistry.uniqueId = uniqueId
                localAccountManager.setUniqueId(uniqueId)
                startMainActivity(activity)
                loadingViewModel.hide()
            }

            is LoginEvent.Notice -> {
                loadingViewModel.hide()
                snackbarHostState.showSnackbar(event.message)
            }
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

        val id = intent.getStringExtra(IntentKey.ID) ?: return@rememberLauncherForActivityResult
        val password =
            intent.getStringExtra(IntentKey.PASSWORD) ?: return@rememberLauncherForActivityResult

        loginViewModel.setIdAndPassword(id, password)
    }
}

private fun startMainActivity(
    context: Context
) {
    val intent = Intent(context, MainActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

private fun startRegisterActivity(
    context: Context,
    registerResultLauncher: ActivityResultLauncher<Intent>
) {
    val intent = Intent(context, RegisterActivity::class.java)
    registerResultLauncher.launch(intent)
}