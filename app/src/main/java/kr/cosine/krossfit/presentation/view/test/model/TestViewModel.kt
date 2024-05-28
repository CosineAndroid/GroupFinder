package kr.cosine.krossfit.presentation.view.test.model

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.cosine.krossfit.domain.usecase.TestUseCase
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val testUseCase: TestUseCase
) : ViewModel() {

    fun onTest() {
        val testEntity = testUseCase()
        Log.d("KrossFitTest", testEntity.test)
    }
}