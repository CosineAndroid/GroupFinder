package kr.cosine.groupfinder.presentation.view.account.login.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.collectLatest
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.manager.LocalAccountManager
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.presentation.MainActivity
import kr.cosine.groupfinder.presentation.view.account.component.AccountScaffold
import kr.cosine.groupfinder.presentation.view.account.login.event.LoginEvent
import kr.cosine.groupfinder.presentation.view.account.login.model.LoginViewModel
import kr.cosine.groupfinder.presentation.view.account.register.RegisterActivity
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.common.data.ResultCode
import kr.cosine.groupfinder.presentation.view.common.extension.launch
import kr.cosine.groupfinder.presentation.view.common.extension.startNewActivity
import kr.cosine.groupfinder.presentation.view.common.model.LoginSessionViewModel
import kr.cosine.groupfinder.presentation.view.compose.component.BaseButton
import kr.cosine.groupfinder.presentation.view.compose.component.BaseCheckbox
import kr.cosine.groupfinder.presentation.view.compose.component.BaseTextField
import kr.cosine.groupfinder.presentation.view.compose.component.LoadingScreen
import kr.cosine.groupfinder.presentation.view.compose.component.LocalSnackbar
import kr.cosine.groupfinder.presentation.view.compose.component.Space
import kr.cosine.groupfinder.presentation.view.compose.component.getActivityResultLauncher
import kr.cosine.groupfinder.presentation.view.compose.data.Snackbar
import kr.cosine.groupfinder.presentation.view.compose.extension.currentComponentActivity
import kr.cosine.groupfinder.presentation.view.compose.model.LoadingViewModel
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor
import kr.cosine.groupfinder.util.MyFirebaseMessagingService

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
) {
    AccountScaffold {
        val activity = LocalContext.currentComponentActivity
        val localAccountManager = LocalAccountManager(activity)
        LoadingScreen()
        LoginLaunchedEffect(localAccountManager)
        AutoLoginLaunchedEffect(localAccountManager) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                modifier = Modifier
                    .fillMaxSize()
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
                BaseCheckbox(
                    isChecked = localAccountManager.isAutoLogin(),
                    text = stringResource(R.string.login_auto_login_title),
                    onCheckedChange = localAccountManager::setAutoLogin
                )
                Space(
                    height = 10.dp
                )
                BaseButton(
                    text = stringResource(R.string.login),
                    containerColor = BaseColor.AccountLoginButtonBackground
                ) {
                    loadingViewModel.show()
                    loginViewModel.loginByInput()
                }
                val context = LocalContext.current
                val registerResultLauncher = getRegisterResultLanuncher()
                BaseButton(
                    text = stringResource(R.string.register),
                    containerColor = BaseColor.Background,
                    elevation = 0.dp
                ) {
                    context.launch(RegisterActivity::class, registerResultLauncher)
                }
            }
        }
    }
}

@Composable
private fun LoginLaunchedEffect(
    localAccountManager: LocalAccountManager,
    loginViewModel: LoginViewModel = hiltViewModel(),
    loginSessionViewModel: LoginSessionViewModel = hiltViewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
) {
    val activity = LocalContext.currentComponentActivity
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackbar = LocalSnackbar.current
    LaunchedEffect(
        key1 = Unit
    ) {
        onLoginEvent(
            localAccountManager,
            activity,
            lifecycle,
            snackbar,
            loginViewModel,
            loginSessionViewModel,
            loadingViewModel
        )
    }
}

@Composable
private fun AutoLoginLaunchedEffect(
    localAccountManager: LocalAccountManager,
    loginViewModel: LoginViewModel = hiltViewModel(),
    loadingViewModel: LoadingViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    val uniqueId = localAccountManager.findUniqueId()
    if (!LocalAccountRegistry.isLogout && localAccountManager.isAutoLogin() && uniqueId != null) {
        loadingViewModel.show()
        LaunchedEffect(
            key1 = Unit
        ) {
            loginViewModel.loginByUniqueId(uniqueId)
            return@LaunchedEffect
        }
    }
    content()
}

private suspend fun onLoginEvent(
    localAccountManager: LocalAccountManager,
    activity: ComponentActivity,
    lifecycle: Lifecycle,
    snackbar: Snackbar,
    loginViewModel: LoginViewModel,
    logionSessionViewModel: LoginSessionViewModel,
    loadingViewModel: LoadingViewModel
) {
    loginViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
        when (event) {
            is LoginEvent.Notice -> {
                loadingViewModel.hide()
                snackbar.show(event.message)
            }

            is LoginEvent.Success -> {
                LocalAccountRegistry.isLogout = false

                val uniqueId = event.accountEntity.uniqueId
                LocalAccountRegistry.setUniqueId(uniqueId)
                localAccountManager.setUniqueId(uniqueId)
                loginViewModel.refreshLastLogin()
                logionSessionViewModel.addLoginSession()

                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    val token = task.result
                    MyFirebaseMessagingService().sendRegistrationToServer(token, uniqueId)
                }

                activity.startNewActivity(MainActivity::class)
            }
        }
    }
}

@Composable
private fun getRegisterResultLanuncher(
    loginViewModel: LoginViewModel = viewModel()
): ActivityResultLauncher<Intent> {
    return getActivityResultLauncher { result ->
        if (result.resultCode != ResultCode.SUCCESS_REGISTER_ACCOUNT) return@getActivityResultLauncher
        val intent = result.data ?: return@getActivityResultLauncher

        val id = intent.getStringExtra(IntentKey.ID) ?: return@getActivityResultLauncher
        val password = intent.getStringExtra(IntentKey.PASSWORD) ?: return@getActivityResultLauncher

        loginViewModel.setIdAndPassword(id, password)
    }
}