package kr.cosine.groupfinder.presentation.view.compose.component

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
fun getActivityResultLauncher(
    onResult: (ActivityResult) -> Unit
) : ActivityResultLauncher<Intent> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = onResult
    )
}