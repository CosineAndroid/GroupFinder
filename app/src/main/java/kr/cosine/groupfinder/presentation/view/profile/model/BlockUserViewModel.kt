package kr.cosine.groupfinder.presentation.view.profile.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.domain.exception.AccountNotExistsException
import kr.cosine.groupfinder.domain.usecase.GetBlockUserUseCase
import kr.cosine.groupfinder.domain.usecase.UnblockUserUseCase
import kr.cosine.groupfinder.presentation.view.profile.state.BlockUserUiState
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BlockUserViewModel @Inject constructor(
    private val getBlockUserUseCase: GetBlockUserUseCase,
    private val unblockUserUseCase: UnblockUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BlockUserUiState>(BlockUserUiState.Loading)
    val uiState: StateFlow<BlockUserUiState> get() = _uiState

    fun loadBlockUser() = viewModelScope.launch(Dispatchers.IO) {
        getBlockUserUseCase(LocalAccountRegistry.uniqueId).onSuccess { blockUserItems ->
            _uiState.update {
                if (blockUserItems.isEmpty()) {
                    BlockUserUiState.Empty
                } else {
                    BlockUserUiState.Success(blockUserItems)
                }
            }
        }.onFailure { throwable ->
            _uiState.update {
                when (throwable) {
                    is AccountNotExistsException -> BlockUserUiState.LoadFail
                    else -> BlockUserUiState.UnknownFail
                }
            }
        }
    }

    fun unblock(blockedUserUniqueId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        unblockUserUseCase(LocalAccountRegistry.uniqueId, blockedUserUniqueId)
    }
}