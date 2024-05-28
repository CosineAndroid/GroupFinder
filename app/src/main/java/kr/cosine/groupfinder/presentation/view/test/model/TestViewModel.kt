package kr.cosine.groupfinder.presentation.view.test.model

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.cosine.groupfinder.domain.usecase.TestUseCase
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val testUseCase: TestUseCase
) : ViewModel() {

    fun onTest() {
        val testEntity = testUseCase()
        Log.d("GroupFinderTest", testEntity.test)
    }
}