package kr.cosine.groupfinder.presentation.view.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.databinding.FragmentGroupBinding
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.common.RefreshableFragment
import kr.cosine.groupfinder.presentation.view.common.data.IntentKey
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.common.extension.applyWhite
import kr.cosine.groupfinder.presentation.view.common.extension.requireContext
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.common.extension.setOnRefreshListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.common.extension.showToast
import kr.cosine.groupfinder.presentation.view.detail.DetailActivity
import kr.cosine.groupfinder.presentation.view.group.adapter.GroupAdpater
import kr.cosine.groupfinder.presentation.view.group.adapter.decoration.GroupTagItemDecoration
import kr.cosine.groupfinder.presentation.view.group.model.GroupViewModel
import kr.cosine.groupfinder.presentation.view.group.state.GroupUiState
import kr.cosine.groupfinder.presentation.view.group.state.item.GroupItem
import kr.cosine.groupfinder.presentation.view.tag.adapter.TagAdapter
import kr.cosine.groupfinder.presentation.view.tag.event.TagEvent
import kr.cosine.groupfinder.presentation.view.tag.model.TagViewModel
import kr.cosine.groupfinder.presentation.view.tag.sheet.TagBottomSheetFragment
import kr.cosine.groupfinder.presentation.view.write.WriteActivity

@AndroidEntryPoint
class GroupFragment(
    val mode: Mode? = null
) : RefreshableFragment() {

    private var _binding: FragmentGroupBinding? = null
    private val binding: FragmentGroupBinding get() = _binding!!

    private val groupViewModel by viewModels<GroupViewModel>()
    private val tagViewModel by activityViewModels<TagViewModel>()

    private lateinit var groupAdpater: GroupAdpater
    private lateinit var tagAdapter: TagAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetTagViewModel()
        registerProgressBar()
        registerSwipeRefreshLayout()
        registerGroupRecyclerView()
        registerTagRecyclerView()
        registerSearchBarButton()
        registerWriteButton()
        registerGroupViewModelEvent()
        registerTagViewModel()
    }

    override fun refresh() {
        groupViewModel.onSearch(mode, tagViewModel.tags)
    }

    private fun resetTagViewModel() {
        tagViewModel.clearTags()
    }

    private fun registerProgressBar() {
        binding.progressBar.applyWhite()
    }

    private fun registerSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListenerWithCooldown(
            fail = {
                requireContext.showToast(R.string.group_refresh_cooldown_message, it)
            }
        ) {
            refresh()
        }
    }

    private fun registerGroupRecyclerView() {
        binding.groupRecyclerView.adapter = GroupAdpater(this::openDetailActivity).apply {
            groupAdpater = this
        }
    }

    private fun openDetailActivity(group: GroupItem) {
        launch(DetailActivity::class) {
            putExtra(IntentKey.POST_UNIQUE_ID, group.postUniqueId)
        }
    }

    private fun registerTagRecyclerView() = with(binding.tagRecyclerView) {
        adapter = TagAdapter(tagViewModel.tags.toMutableList(), tagViewModel::removeTag).apply {
            tagAdapter = this
        }
        addItemDecoration(GroupTagItemDecoration)
    }

    private fun registerSearchBarButton() = with(binding) {
        clearTagImageButton.setOnClickListener {
            tagViewModel.clearTags()
        }
        showAllTagImageButton.setOnClickListenerWithCooldown(Interval.OPEN_SCREEN) {
            showTagBottomSheetFragment()
        }
        searchImageButton.setOnClickListener {
            refresh()
        }
    }

    private fun showTagBottomSheetFragment() {
        TagBottomSheetFragment.show(childFragmentManager)
    }

    private fun registerWriteButton() {
        binding.writeImageButton.setOnClickListener {
            launch(WriteActivity::class) {
                putExtra(IntentKey.MODE, mode ?: Mode.NORMAL)
            }
        }
    }

    private fun registerGroupViewModelEvent() = with(binding) {
        groupViewModel.onSearch(mode, emptySet())
        viewLifecycleOwner.lifecycleScope.launch {
            groupViewModel.uiState.flowWithLifecycle(lifecycle).collectLatest { uiState ->
                val isLoading = uiState is GroupUiState.Loading
                val isNotice = uiState is GroupUiState.Notice

                progressBar.isVisible = isLoading
                searchResultNoticeTextView.isVisible = isNotice
                groupRecyclerView.isVisible = !isLoading

                searchImageButton.isEnabled = !isLoading
                clearTagImageButton.isEnabled = !isLoading
                showAllTagImageButton.isEnabled = !isLoading
                swipeRefreshLayout.isEnabled = !isLoading

                when (uiState) {
                    is GroupUiState.Success -> groupAdpater.setGroups(uiState.posts)

                    is GroupUiState.Notice -> {
                        if (uiState is GroupUiState.Empty) {
                            groupAdpater.clearGroups()
                        }
                        searchResultNoticeTextView.text = uiState.message
                    }

                    else -> {}
                }
            }
        }
    }

    private fun registerTagViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            tagViewModel.event.flowWithLifecycle(lifecycle).collectLatest { event ->
                when (event) {
                    is TagEvent.SetTag -> tagAdapter.setTags(event.tags)
                    is TagEvent.AddTag -> tagAdapter.addTag(event.tag)
                    is TagEvent.RemoveTag -> tagAdapter.removeTag(event.tag)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}