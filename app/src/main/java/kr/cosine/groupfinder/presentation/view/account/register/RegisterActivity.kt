package kr.cosine.groupfinder.presentation.view.account.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.presentation.view.account.component.BaseButton
import kr.cosine.groupfinder.presentation.view.account.component.DefaultTextField
import kr.cosine.groupfinder.presentation.view.account.extension.showToast
import kr.cosine.groupfinder.presentation.view.account.intent.IntentKey
import kr.cosine.groupfinder.presentation.view.account.login.LoginActivity
import kr.cosine.groupfinder.presentation.view.account.register.component.InfoTextField
import kr.cosine.groupfinder.presentation.view.account.register.event.RegisterEvent
import kr.cosine.groupfinder.presentation.view.account.register.message.Message
import kr.cosine.groupfinder.presentation.view.account.register.model.RegisterViewModel
import kr.cosine.groupfinder.presentation.view.account.register.state.RegisterErrorUiState
import kr.cosine.groupfinder.presentation.view.account.ui.CustomColor

@AndroidEntryPoint
class RegisterActivity : ComponentActivity() {

    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Register()
        }
        registerViewModelEvent()
    }

    private fun registerViewModelEvent() {
        lifecycleScope.launch {
            registerViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
                when (event) {
                    is RegisterEvent.Success -> {
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                            putExtra(IntentKey.ID, event.id)
                            putExtra(IntentKey.PASSWORD, event.password)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                        showToast(Message.SUCCESS)
                    }
                    is RegisterEvent.IdDuplicationFail -> showToast(Message.ID_DUPLICATION)
                    is RegisterEvent.TaggedNicknameDuplicationFail -> showToast(Message.TAGGED_NICKNAME_DUPLICATION)
                    is RegisterEvent.UnknownFail -> showToast(Message.UNKNOWN_ERROR)
                }
            }
        }
    }
}

private val RegisterErrorUiState.color get() = if (this is RegisterErrorUiState.Valid) {
    CustomColor.RegisterValidBorder
} else {
    CustomColor.RegisterInvalidBorder
}

@Composable
private fun Register(
    registerViewModel: RegisterViewModel = viewModel()
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
            registerViewModel.register()
        }
    }
}
