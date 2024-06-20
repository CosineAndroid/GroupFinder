package kr.cosine.groupfinder.presentation.view.account.register.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.manager.LocalAccountManager
import kr.cosine.groupfinder.presentation.view.account.component.AccountScaffold
import kr.cosine.groupfinder.presentation.view.account.register.event.RegisterEvent
import kr.cosine.groupfinder.presentation.view.account.register.model.RegisterViewModel
import kr.cosine.groupfinder.presentation.view.account.register.screen.component.HeightSpace
import kr.cosine.groupfinder.presentation.view.account.register.screen.component.InfoTextField
import kr.cosine.groupfinder.presentation.view.account.register.state.RegisterErrorUiState
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.common.data.ResultCode
import kr.cosine.groupfinder.presentation.view.common.extension.startActivity
import kr.cosine.groupfinder.presentation.view.compose.component.BaseButton
import kr.cosine.groupfinder.presentation.view.compose.component.BaseCheckbox
import kr.cosine.groupfinder.presentation.view.compose.component.BaseText
import kr.cosine.groupfinder.presentation.view.compose.component.DefaultTextField
import kr.cosine.groupfinder.presentation.view.compose.component.LoadingScreen
import kr.cosine.groupfinder.presentation.view.compose.component.LocalSnackbar
import kr.cosine.groupfinder.presentation.view.compose.data.Snackbar
import kr.cosine.groupfinder.presentation.view.compose.extension.currentActivity
import kr.cosine.groupfinder.presentation.view.compose.model.LoadingViewModel
import kr.cosine.groupfinder.presentation.view.compose.ui.BaseColor
import kr.cosine.groupfinder.presentation.view.profile.PolicyActivity

private val RegisterErrorUiState.color
    get() = when (this) {
        is RegisterErrorUiState.Blank -> BaseColor.RegisterEmptyBorder
        is RegisterErrorUiState.Valid -> BaseColor.RegisterValidBorder
        else -> BaseColor.RegisterInvalidBorder
    }

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
) {
    AccountScaffold {
        LoadingScreen()
        RegisterLaunchedEffect()
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
        ) {
            BaseText(
                text = "회원가입",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            HeightSpace()
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
            BaseCheckbox(
                isChecked = false,
                text = stringResource(R.string.register_age_agreement_title),
                onCheckedChange = registerViewModel::checkAgeCheckbox
            )
            val activity = LocalContext.currentActivity
            BaseCheckbox(
                isChecked = false,
                text = stringResource(R.string.register_policy_agreement_title),
                underline = true,
                isTextClickEnabled = true,
                onTextClick = activity::startPolicyActivity,
                onCheckedChange = registerViewModel::checkPolicyCheckbox
            )
            HeightSpace()
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

@Composable
private fun RegisterLaunchedEffect(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    loadingViewModel: LoadingViewModel = viewModel()
) {
    val activity = LocalContext.currentActivity
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val snackbar = LocalSnackbar.current
    LaunchedEffect(
        key1 = Unit
    ) {
        onRegisterEvent(
            activity,
            lifecycle,
            snackbar,
            registerViewModel,
            loadingViewModel
        )
    }
}

private suspend fun onRegisterEvent(
    activity: ComponentActivity,
    lifecycle: Lifecycle,
    snackbar: Snackbar,
    registerViewModel: RegisterViewModel,
    loadingViewModel: LoadingViewModel
) {
    registerViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
        when (event) {
            is RegisterEvent.Notice -> {
                loadingViewModel.hide()
                snackbar.show(event.message)
            }

            is RegisterEvent.Success -> {
                val accountEntity = event.accountEntity

                val localAccountManager = LocalAccountManager(activity)
                localAccountManager.setUniqueId(accountEntity.uniqueId)

                val intent = Intent().apply {
                    putExtra(IntentKey.ID, accountEntity.id)
                    putExtra(IntentKey.PASSWORD, accountEntity.password)
                }
                activity.setResult(ResultCode.SUCCESS_REGISTER_ACCOUNT, intent)
                activity.finish()
                loadingViewModel.hide()
            }
        }
    }
}

private fun Context.startPolicyActivity() {
    startActivity(PolicyActivity::class) {
        putExtra(IntentKey.USE_LOGIN_SESSION, false)
    }
}