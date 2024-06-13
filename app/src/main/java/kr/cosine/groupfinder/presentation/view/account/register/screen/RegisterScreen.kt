package kr.cosine.groupfinder.presentation.view.account.register.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.manager.LocalAccountManager
import kr.cosine.groupfinder.presentation.view.compose.component.BaseButton
import kr.cosine.groupfinder.presentation.view.compose.component.BaseScaffold
import kr.cosine.groupfinder.presentation.view.compose.component.DefaultTextField
import kr.cosine.groupfinder.presentation.view.compose.component.LoadingScreen
import kr.cosine.groupfinder.presentation.view.account.login.LoginActivity
import kr.cosine.groupfinder.presentation.view.compose.model.LoadingViewModel
import kr.cosine.groupfinder.presentation.view.account.register.screen.component.InfoTextField
import kr.cosine.groupfinder.presentation.view.account.register.event.RegisterEvent
import kr.cosine.groupfinder.presentation.view.account.register.model.RegisterViewModel
import kr.cosine.groupfinder.presentation.view.account.register.state.RegisterErrorUiState
import kr.cosine.groupfinder.presentation.view.common.data.Code
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.compose.component.Space
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor

private val RegisterErrorUiState.color
    get() = when (this) {
        is RegisterErrorUiState.Blank -> BaseColor.RegisterEmptyBorder
        is RegisterErrorUiState.Valid -> BaseColor.RegisterValidBorder
        else -> BaseColor.RegisterInvalidBorder
    }

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = viewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
) {
    val activity = LocalContext.current as ComponentActivity
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LoadingScreen()
    BaseScaffold(
        prevBody = { snackbarHostState ->
            LaunchedEffect(
                key1 = Unit
            ) {
                onRegisterEvent(activity, lifecycle, snackbarHostState, registerViewModel, loadingViewModel)
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
        ) {
            val uiState by registerViewModel.uiState.collectAsStateWithLifecycle()
            DefaultTextField(
                hint = stringResource(R.string.register_id_hint),
                borderColor = uiState.id.color,
                onValueChange = registerViewModel::checkId
            )
            DefaultTextField(
                hint = stringResource(R.string.register_password_hint),
                visualTransformation = PasswordVisualTransformation(),
                borderColor = uiState.password.color,
                onValueChange = registerViewModel::checkPassword
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            ) {
                InfoTextField(
                    text = stringResource(R.string.nickname_hint),
                    borderColor = uiState.nickname.color,
                    onValueChange = registerViewModel::checkNickname
                )
                InfoTextField(
                    text = stringResource(R.string.tag_hint),
                    borderColor = uiState.tag.color,
                    onValueChange = registerViewModel::checkTag
                )
            }
            Space(height = 20.dp)
            registerViewModel.checkButtonEnable()
            BaseButton(
                isEnabled = uiState.isButtonEnabled,
                text = stringResource(R.string.register)
            ) {
                loadingViewModel.show()
                registerViewModel.register()
            }
        }
    }
}

private suspend fun onRegisterEvent(
    activity: ComponentActivity,
    lifecycle: Lifecycle,
    snackbarHostState: SnackbarHostState,
    registerViewModel: RegisterViewModel,
    loadingViewModel: LoadingViewModel
) {
    registerViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
        when (event) {
            is RegisterEvent.Success -> {
                val accountEntity = event.accountEntity

                val localAccountManager = LocalAccountManager(activity)
                localAccountManager.setUniqueId(accountEntity.uniqueId)

                val intent = Intent(activity, LoginActivity::class.java).apply {
                    putExtra(IntentKey.ID, accountEntity.id)
                    putExtra(IntentKey.PASSWORD, accountEntity.password)
                }
                activity.setResult(Code.SUCCESS_REGISTER_ACCOUNT, intent)
                activity.finish()
                loadingViewModel.hide()
            }

            is RegisterEvent.Notice -> {
                loadingViewModel.hide()
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }
}