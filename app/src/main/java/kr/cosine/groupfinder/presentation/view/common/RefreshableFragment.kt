package kr.cosine.groupfinder.presentation.view.common

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kr.cosine.groupfinder.presentation.view.common.data.ResultCode
import kotlin.reflect.KClass

abstract class RefreshableFragment : Fragment() {

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerActivityResultLauncher()
    }

    private fun registerActivityResultLauncher() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode != ResultCode.REFRESH) return@registerForActivityResult
            refresh()
        }
    }

    protected abstract fun refresh()

    protected fun <T : Any> launch(clazz: KClass<T>, intentScope: Intent.() -> Unit = {}) {
        val intent = Intent(context, clazz.java).apply(intentScope)
        activityResultLauncher.launch(intent)
    }
}