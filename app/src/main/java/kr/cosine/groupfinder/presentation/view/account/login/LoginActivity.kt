package kr.cosine.groupfinder.presentation.view.account.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kr.cosine.groupfinder.presentation.view.account.component.BaseTextField
import kr.cosine.groupfinder.presentation.view.account.component.Space
import kr.cosine.groupfinder.presentation.view.account.login.component.BaseButton

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BaseTextField(hint = "아이디")
                Space(height = 10.dp)
                BaseTextField(hint = "비밀번호")
                Space(height = 10.dp)
                BaseButton(text = "로그인") {
                    Toast.makeText(this@LoginActivity, "클릭", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}