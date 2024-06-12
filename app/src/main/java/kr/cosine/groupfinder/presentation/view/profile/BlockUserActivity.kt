package kr.cosine.groupfinder.presentation.view.profile

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.databinding.ActivityBlockUserBinding
import kr.cosine.groupfinder.presentation.view.common.extension.applyWhite
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.common.extension.showToast
import kr.cosine.groupfinder.presentation.view.profile.adapter.BlockUserAdapter
import kr.cosine.groupfinder.presentation.view.profile.model.BlockUserViewModel
import kr.cosine.groupfinder.presentation.view.profile.state.BlockUserUiState
import kr.cosine.groupfinder.presentation.view.profile.state.item.BlockUserItem

@AndroidEntryPoint
class BlockUserActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBlockUserBinding.inflate(layoutInflater) }

    private val blockUserViewModel by viewModels<BlockUserViewModel>()

    private lateinit var blockUserAdapter: BlockUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerProgressBar()
        registerCloseButton()
        registerRecyclerView()
        registerViewModelEvent()
    }

    private fun registerProgressBar() {
        binding.progressBar.applyWhite()
    }

    private fun registerCloseButton() {
        binding.closeImageButton.setOnClickListenerWithCooldown {
            finish()
        }
    }

    private fun registerRecyclerView() = with(binding.blockUserRecyclerView) {
        adapter = BlockUserAdapter {
            blockUserViewModel.unblock(it.uniqueId)
            showToast("${it.nickname}#${it.tag}님의 차단을 해제하였습니다.")
        }.apply {
            blockUserAdapter = this
        }
        AppCompatResources.getDrawable(context, R.drawable.block_user_item_decoration)?.let {
            val dividerItemDecoration = DividerItemDecoration(context, LinearLayout.VERTICAL).apply {
                setDrawable(it)
            }
            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun registerViewModelEvent() = with(binding) {
        blockUserViewModel.loadBlockUser()
        lifecycleScope.launch {
            blockUserViewModel.uiState.flowWithLifecycle(lifecycle).collectLatest { uiState ->
                val isLoading = uiState is BlockUserUiState.Loading

                progressBar.isVisible = isLoading
                resultNoticeTextView.isVisible = uiState is BlockUserUiState.Notice

                when (uiState) {
                    is BlockUserUiState.Notice -> resultNoticeTextView.text = uiState.message

                    is BlockUserUiState.Success -> blockUserAdapter.setBlockUsers(uiState.blockUsers)

                    else -> {}
                }
            }
        }
    }
}