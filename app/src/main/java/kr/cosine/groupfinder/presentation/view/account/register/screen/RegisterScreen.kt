package kr.cosine.groupfinder.presentation.view.account.register.screen

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.presentation.view.account.component.BaseButton
import kr.cosine.groupfinder.presentation.view.account.component.DefaultTextField
import kr.cosine.groupfinder.presentation.view.account.extension.showToast
import kr.cosine.groupfinder.presentation.view.account.intent.IntentKey
import kr.cosine.groupfinder.presentation.view.account.login.LoginActivity
import kr.cosine.groupfinder.presentation.view.account.register.screen.component.InfoTextField
import kr.cosine.groupfinder.presentation.view.account.register.event.RegisterEvent
import kr.cosine.groupfinder.presentation.view.account.register.message.Message
import kr.cosine.groupfinder.presentation.view.account.register.model.RegisterViewModel
import kr.cosine.groupfinder.presentation.view.account.register.state.RegisterErrorUiState
import kr.cosine.groupfinder.presentation.view.account.ui.CustomColor

private val RegisterErrorUiState.color
    get() = if (this is RegisterErrorUiState.Valid) {
        CustomColor.RegisterValidBorder
    } else {
        CustomColor.RegisterInvalidBorder
    }

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = viewModel()
) {
    // compose lifecycleowner
    val activity = LocalContext.current as ComponentActivity
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(
        key1 = Unit
    ) {
        onRegisterEvent(activity, lifecycle, registerViewModel)
    }
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
            // 로딩창
            registerViewModel.register()
        }
    }
}

private suspend fun onRegisterEvent(
    activity: ComponentActivity,
    lifecycle: Lifecycle,
    registerViewModel: RegisterViewModel
) {
    registerViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
        when (event) {
            is RegisterEvent.Success -> {
                val intent = Intent(activity, LoginActivity::class.java).apply {
                    putExtra(IntentKey.ID, event.id)
                    putExtra(IntentKey.PASSWORD, event.password)
                }
                activity.setResult(Activity.RESULT_OK, intent)
                activity.finish()
                activity.showToast(Message.SUCCESS)
            }

            is RegisterEvent.IdDuplicationFail -> activity.showToast(Message.ID_DUPLICATION)
            is RegisterEvent.TaggedNicknameDuplicationFail -> activity.showToast(Message.TAGGED_NICKNAME_DUPLICATION)
            is RegisterEvent.UnknownFail -> activity.showToast(Message.UNKNOWN_ERROR)
        }
    }
}