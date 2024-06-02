package kr.cosine.groupfinder.presentation.view.account.register

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.cosine.groupfinder.presentation.view.account.component.BaseTextField
import kr.cosine.groupfinder.presentation.view.account.component.BaseButton
import kr.cosine.groupfinder.presentation.view.account.register.component.InfoTextField
import kr.cosine.groupfinder.presentation.view.account.register.model.RegisterViewModel
import kr.cosine.groupfinder.presentation.view.account.register.state.RegisterErrorUiState
import kr.cosine.groupfinder.presentation.view.account.ui.CustomColor

class RegisterActivity : ComponentActivity() {

    private val registerViewModel by viewModels<RegisterViewModel>()

    private val RegisterErrorUiState.color get() = if (this is RegisterErrorUiState.Valid) {
        CustomColor.RegisterValidBorder
    } else {
        CustomColor.RegisterInvalidBorder
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val uiState by registerViewModel.uiState.collectAsStateWithLifecycle()
                BaseTextField(
                    hint = "아이디 (5글자 이상)",
                    borderColor = uiState.id.color,
                    onValueChange = registerViewModel::checkId
                )
                BaseTextField(
                    hint = "비밀번호 (8글자 이상, 숫자/특수문자 포함)",
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
                        text = "닉네임 (16글자 이하)",
                        borderColor = uiState.nickname.color,
                        onValueChange = registerViewModel::checkNickname
                    )
                    InfoTextField(
                        text = "태그 (16글자 이하)",
                        borderColor = uiState.tag.color,
                        onValueChange = registerViewModel::checkTag
                    )
                }
                BaseButton(
                    text = "회원가입",
                    containerColor = CustomColor.RegisterButtonBackground
                ) {

                }
            }
        }
    }
}