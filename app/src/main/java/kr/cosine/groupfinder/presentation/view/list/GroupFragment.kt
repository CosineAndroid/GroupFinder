package kr.cosine.groupfinder.presentation.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.databinding.FragmentGroupBinding
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.list.adapter.GroupAdpater
import kr.cosine.groupfinder.presentation.view.list.adapter.SearchTagAdpater
import kr.cosine.groupfinder.presentation.view.list.adapter.decoration.impl.GroupTagItemDecoration
import kr.cosine.groupfinder.presentation.view.list.event.TagEvent
import kr.cosine.groupfinder.presentation.view.list.model.GroupViewModel
import kr.cosine.groupfinder.presentation.view.list.model.TagViewModel
import kr.cosine.groupfinder.presentation.view.list.state.GroupUiState

@AndroidEntryPoint
class GroupFragment(
    private val mode: Mode? = null
) : Fragment() {

    private var _binding: FragmentGroupBinding? = null
    private val binding: FragmentGroupBinding get() = _binding!!

    private val groupViewModel by viewModels<GroupViewModel>()
    private val tagViewModel by activityViewModels<TagViewModel>()

    private lateinit var groupAdpater: GroupAdpater
    private lateinit var searchTagAdpater: SearchTagAdpater

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
        registerNavigationButton()
        registerGroupRecyclerView()
        registerTagRecyclerView()
        registerSearchBarButton()
        registerWriteButton()
        registerGroupViewModelEvent()
        registerTagViewModel()
    }

    private fun registerNavigationButton() {
        binding.navigationImageButton.setOnClickListener {

        }
    }

    private fun registerGroupRecyclerView() = with(binding.groupRecyclerView) {
        adapter = GroupAdpater(context) { post ->

        }.apply {
            groupAdpater = this
        }
    }

    private fun registerTagRecyclerView() = with(binding.tagRecyclerView) {
        adapter = SearchTagAdpater(tagViewModel::removeTag).apply {
            searchTagAdpater = this
        }
        addItemDecoration(GroupTagItemDecoration)
    }

    private fun registerSearchBarButton() = with(binding) {
        clearTagImageButton.setOnClickListener {
            tagViewModel.clearTag()
        }
        showAllTagImageButton.setOnClickListener {

        }
        searchImageButton.setOnClickListener {
            val tags = tagViewModel.tags
            if (tags.isEmpty()) return@setOnClickListener
            groupViewModel.onSearch(mode, tags)
        }
    }

    private fun registerWriteButton() {
        binding.writeImageButton.setOnClickListener {

        }
    }

    private fun registerGroupViewModelEvent() = with(binding) {
        groupViewModel.onSearch(mode, emptyList())
        viewLifecycleOwner.lifecycleScope.launch {
            groupViewModel.uiState.flowWithLifecycle(lifecycle).collectLatest { uiState ->
                val isLoading = uiState is GroupUiState.Loading
                val isNotice = uiState is GroupUiState.Notice

                searchProgressBar.isVisible = isLoading
                searchResultNoticeTextView.isVisible = isNotice
                groupRecyclerView.isVisible = !isLoading

                searchImageButton.isEnabled = !isLoading
                clearTagImageButton.isEnabled = !isLoading
                showAllTagImageButton.isEnabled = !isLoading

                when (uiState) {
                    is GroupUiState.Result -> groupAdpater.setPosts(uiState.posts)
                    is GroupUiState.Notice -> {
                        if (uiState is GroupUiState.ResultEmpty) {
                            groupAdpater.clearPosts()
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
                    is TagEvent.SetTag -> searchTagAdpater.setTags(event.tags)
                    is TagEvent.AddTag -> searchTagAdpater.addTag(event.tag)
                    is TagEvent.RemoveTag -> searchTagAdpater.removeTag(event.position)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}