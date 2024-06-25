package kr.cosine.groupfinder.presentation.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.manager.LocalAccountManager
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.databinding.FragmentProfileBinding
import kr.cosine.groupfinder.presentation.view.account.login.LoginActivity
import kr.cosine.groupfinder.presentation.view.broadcast.BroadcastListActivity
import kr.cosine.groupfinder.presentation.view.common.RefreshableFragment
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.common.extension.applyWhite
import kr.cosine.groupfinder.presentation.view.common.extension.requireContext
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.common.extension.showToast
import kr.cosine.groupfinder.presentation.view.common.extension.startActivity
import kr.cosine.groupfinder.presentation.view.common.extension.startNewActivity
import kr.cosine.groupfinder.presentation.view.detail.DetailActivity
import kr.cosine.groupfinder.presentation.view.dialog.Dialog
import kr.cosine.groupfinder.presentation.view.dialog.TaggedNicknameInputDialog
import kr.cosine.groupfinder.presentation.view.group.adapter.GroupAdpater
import kr.cosine.groupfinder.presentation.view.profile.event.ProfileChangeEvent
import kr.cosine.groupfinder.presentation.view.profile.event.ProfileEvent
import kr.cosine.groupfinder.presentation.view.profile.model.ProfileViewModel
import kr.cosine.groupfinder.presentation.view.profile.state.ProfileUiState

@AndroidEntryPoint
class ProfileFragment : RefreshableFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel>()

    private lateinit var groupAdpater: GroupAdpater

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerProgressBar()
        registerChangeTaggedNicknameButton()
        registerRecyclerView()
        registerBroadcastButton()
        registerBlockUserButton()
        registerLogoutButton()
        registerWithdrawButton()
        registerPolicyButton()
        registerViewModelEvent()
    }

    override fun refresh() {
        profileViewModel.loadProfile()
    }

    private fun registerProgressBar() {
        binding.progressBar.applyWhite()
    }

    private fun registerChangeTaggedNicknameButton() = with(binding) {
        changeTaggedNicknameImageButton.setOnClickListenerWithCooldown {
            val split = taggedNicknameTextView.text.toString().split("#", limit = 2)
            TaggedNicknameInputDialog(split[0], split[1], profileViewModel::setTaggedNickname)
                .show(parentFragmentManager, TaggedNicknameInputDialog.TAG)
        }
    }

    private fun registerRecyclerView() {
        binding.groupRecyclerView.adapter = GroupAdpater { post ->
            launch(DetailActivity::class) {
                putExtra(IntentKey.POST_UNIQUE_ID, post.postUniqueId)
            }
        }.apply {
            groupAdpater = this
        }
    }

    private fun registerBroadcastButton() {
        binding.broadcastButton.setOnClickListenerWithCooldown {
            val uiState = profileViewModel.uiState.value
            if (uiState is ProfileUiState.Success) {
                requireContext.startActivity(BroadcastListActivity::class) {
                    putExtra(IntentKey.ADMIN, uiState.isAdmin)
                }
            }
        }
    }

    private fun registerBlockUserButton() {
        binding.blockUserButton.setOnClickListenerWithCooldown {
            requireContext.startActivity(BlockUserActivity::class)
        }
    }

    private fun registerLogoutButton() {
        binding.logoutButton.setOnClickListenerWithCooldown {
            showDialog(getString(R.string.profile_logout_message)) {
                resetLocalAccount()
                requireContext.startNewActivity(LoginActivity::class)
            }
        }
    }

    private fun registerWithdrawButton() {
        binding.withdrawButton.setOnClickListenerWithCooldown {
            showDialog(getString(R.string.profile_withdraw_message)) {
                profileViewModel.withdraw()
            }
        }
    }

    private fun showDialog(message: String, onConfirmClick: () -> Unit) {
        Dialog(
            message = message,
            onConfirmClick = onConfirmClick
        ).show(parentFragmentManager, Dialog.TAG)
    }

    private fun registerPolicyButton() {
        binding.policyButton.setOnClickListenerWithCooldown {
            requireContext.startActivity(PolicyActivity::class)
        }
    }

    private fun registerViewModelEvent() = with(binding) {
        refresh()
        lifecycleScope.launch {
            profileViewModel.uiState.flowWithLifecycle(lifecycle).collectLatest { uiState ->
                val isLoading = uiState is ProfileUiState.Loading

                rootConstraintLayout.isVisible = !isLoading
                progressBar.isVisible = isLoading

                if (uiState is ProfileUiState.Success) {
                    setTaggedNickname(uiState.nickname, uiState.tag)
                    val postItem = uiState.groupItem
                    if (postItem == null) {
                        groupRecyclerView.visibility = View.GONE
                    } else {
                        groupAdpater.setGroup(postItem)
                    }
                    if (!uiState.isAdmin) {
                        adminInquiryButton.visibility = View.GONE
                    }
                }
            }
        }
        lifecycleScope.launch {
            profileViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
                when (event) {
                    is ProfileEvent.Notice -> requireContext.showToast(event.message)

                    is ProfileEvent.Success -> {
                        resetLocalAccount()
                        requireContext.startNewActivity(LoginActivity::class)
                    }
                }
            }
        }
        lifecycleScope.launch {
            profileViewModel.changeEvent.flowWithLifecycle(lifecycle).collectLatest { event ->
                if (event is ProfileChangeEvent.Notice) {
                    if (event is ProfileChangeEvent.Success) {
                        setTaggedNickname(event.nickname, event.tag)
                    }
                    requireContext.showToast(event.message)
                }
            }
        }
    }

    private fun setTaggedNickname(nickname: String, tag: String) {
        binding.taggedNicknameTextView.text = getString(
            R.string.tagged_nickname_format,
            nickname,
            tag
        )
    }

    private fun resetLocalAccount() {
        LocalAccountRegistry.isLogout = true
        LocalAccountRegistry.resetUniqueId()
        LocalAccountManager(requireContext).reset()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}