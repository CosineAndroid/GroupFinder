package kr.cosine.groupfinder.presentation.view.account.register.screen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import kr.cosine.groupfinder.presentation.view.account.component.BaseButton
import kr.cosine.groupfinder.presentation.view.account.component.DefaultTextField
import kr.cosine.groupfinder.presentation.view.account.component.LoadingScreen
import kr.cosine.groupfinder.presentation.view.account.intent.IntentKey
import kr.cosine.groupfinder.presentation.view.account.login.LoginActivity
import kr.cosine.groupfinder.presentation.view.account.model.LoadingViewModel
import kr.cosine.groupfinder.presentation.view.account.register.screen.component.InfoTextField
import kr.cosine.groupfinder.presentation.view.account.register.event.RegisterEvent
import kr.cosine.groupfinder.presentation.view.account.message.Message
import kr.cosine.groupfinder.presentation.view.account.register.model.RegisterViewModel
import kr.cosine.groupfinder.presentation.view.account.register.state.RegisterErrorUiState
import kr.cosine.groupfinder.presentation.view.account.ui.CustomColor

private val RegisterErrorUiState.color
    get() = if (this is RegisterErrorUiState.Valid) {
        CustomColor.RegisterValidBorder
    } else {
        CustomColor.RegisterInvalidBorder
    }

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = viewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
) {
    val activity = LocalContext.current as ComponentActivity
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackbarHostState = remember { SnackbarHostState() }
    LoadingScreen()
    LaunchedEffect(
        key1 = Unit
    ) {
        onRegisterEvent(activity, lifecycle, snackbarHostState, registerViewModel, loadingViewModel)
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
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
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
                    text = stringResource(R.string.register_nickname_hint),
                    borderColor = uiState.nickname.color,
                    onValueChange = registerViewModel::checkNickname
                )
                InfoTextField(
                    text = stringResource(R.string.register_tag_hint),
                    borderColor = uiState.tag.color,
                    onValueChange = registerViewModel::checkTag
                )
            }
            registerViewModel.checkButtonEnable()
            BaseButton(
                isEnabled = uiState.isButtonEnabled,
                text = stringResource(R.string.register),
                containerColor = CustomColor.RegisterButtonBackground
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
        loadingViewModel.hide()
        when (event) {
            is RegisterEvent.Success -> {
                val accountEntity = event.accountEntity
                val intent = Intent(activity, LoginActivity::class.java).apply {
                    putExtra(IntentKey.ID, accountEntity.id)
                    putExtra(IntentKey.PASSWORD, accountEntity.password)
                }
                activity.setResult(Activity.RESULT_OK, intent)
                activity.finish()
            }

            is RegisterEvent.IdDuplicationFail -> snackbarHostState.showSnackbar(Message.ID_DUPLICATION)

            is RegisterEvent.TaggedNicknameDuplicationFail -> snackbarHostState.showSnackbar(Message.TAGGED_NICKNAME_DUPLICATION)

            is RegisterEvent.UnknownFail -> snackbarHostState.showSnackbar(Message.UNKNOWN_ERROR)
        }
    }
}