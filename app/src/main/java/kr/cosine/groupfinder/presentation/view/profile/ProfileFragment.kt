package kr.cosine.groupfinder.presentation.view.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.common.extension.applyWhite
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.common.extension.showToast
import kr.cosine.groupfinder.presentation.view.common.util.ActivityUtil
import kr.cosine.groupfinder.presentation.view.list.adapter.GroupAdpater
import kr.cosine.groupfinder.presentation.view.profile.event.ProfileEvent
import kr.cosine.groupfinder.presentation.view.profile.model.ProfileViewModel
import kr.cosine.groupfinder.presentation.view.profile.state.ProfileUiState

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by viewModels<ProfileViewModel>()

    private lateinit var groupAdpater: GroupAdpater

    private val requireContext get() = requireContext()

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
        registerRecyclerView()
        registerLogoutButton()
        registerWithdrawButton()
        registerViewModelEvent()
    }

    private fun registerProgressBar() {
        binding.progressBar.applyWhite()
    }

    private fun registerRecyclerView() = with(binding.groupRecyclerView) {
        adapter = GroupAdpater(context) { post ->

        }.apply {
            groupAdpater = this
        }
        suppressLayout(true)
    }

    private fun registerLogoutButton() {
        binding.logoutButton.setOnClickListenerWithCooldown(Interval.OPEN_SCREEN) {
            showDialog(getString(R.string.profile_logout_message)) {
                LocalAccountRegistry.isLogout = true
                LocalAccountRegistry.resetUniqueId()
                ActivityUtil.startNewActivity(requireContext, LoginActivity::class)
            }
        }
    }

    private fun registerWithdrawButton() {
        binding.withdrawButton.setOnClickListenerWithCooldown(Interval.OPEN_SCREEN) {
            showDialog(getString(R.string.profile_withdraw_message)) {
                profileViewModel.withdraw(LocalAccountRegistry.uniqueId)
            }
        }
    }

    private fun showDialog(message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext).apply {
            setCancelable(false)
            setMessage(message)
            setNegativeButton(getString(R.string.dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(getString(R.string.dialog_confirm)) { _, _ ->
                onConfirm()
            }
        }.show()
    }

    private fun registerViewModelEvent() = with(binding) {
        profileViewModel.loadProfile(LocalAccountRegistry.uniqueId)
        lifecycleScope.launch {
            profileViewModel.uiState.flowWithLifecycle(lifecycle).collectLatest { uiState ->
                val isLoading = uiState is ProfileUiState.Loading

                totalConstraintLayout.isVisible = !isLoading
                progressBar.isVisible = isLoading

                if (uiState is ProfileUiState.Success) {
                    taggedNicknameTextView.text = getString(
                        R.string.profile_tagged_nickname_format,
                        uiState.nickname,
                        uiState.tag
                    )
                    val postItem = uiState.groupItem
                    if (postItem != null) {
                        groupAdpater.setPost(postItem)
                    }
                }
            }
        }
        lifecycleScope.launch {
            profileViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
                when (event) {
                    is ProfileEvent.Notice -> requireContext.showToast(event.message)

                    is ProfileEvent.Success -> {
                        LocalAccountRegistry.isLogout = true
                        LocalAccountManager(requireContext).reset()
                        ActivityUtil.startNewActivity(requireContext, LoginActivity::class)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}