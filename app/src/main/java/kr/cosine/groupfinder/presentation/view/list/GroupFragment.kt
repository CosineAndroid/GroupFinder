package kr.cosine.groupfinder.presentation.view.list

import android.content.Intent
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
import kr.cosine.groupfinder.presentation.view.list.adapter.SearchTagAdapter
import kr.cosine.groupfinder.presentation.view.list.adapter.decoration.impl.GroupTagItemDecoration
import kr.cosine.groupfinder.presentation.view.list.event.TagEvent
import kr.cosine.groupfinder.presentation.view.list.model.GroupViewModel
import kr.cosine.groupfinder.presentation.view.list.model.TagViewModel
import kr.cosine.groupfinder.presentation.view.list.state.GroupUiState
import kr.cosine.groupfinder.presentation.view.test.model.PostViewModel
import kr.cosine.groupfinder.presentation.view.write.WriteActivity

@AndroidEntryPoint
class GroupFragment(
    private val mode: Mode? = null
) : Fragment() {

    private var _binding: FragmentGroupBinding? = null
    private val binding: FragmentGroupBinding get() = _binding!!

    private val groupViewModel by viewModels<GroupViewModel>()
    private val tagViewModel by activityViewModels<TagViewModel>()
    private val postViewModel by viewModels<PostViewModel>()

    private lateinit var groupAdpater: GroupAdpater
    private lateinit var searchTagAdapter: SearchTagAdapter

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
        registerSwipeRefreshLayout()
        registerGroupRecyclerView()
        registerTagRecyclerView()
        registerSearchBarButton()
        registerWriteButton()
        registerGroupViewModelEvent()
        registerTagViewModel()
    }

    private fun registerNavigationButton() {
        /*binding.navigationImageButton.setOnClickListener {

        }*/
    }

    private fun registerSwipeRefreshLayout() = with(binding.swipeRefreshLayout) {
        setOnRefreshListener {
            isRefreshing = false
            search()
        }
    }

    private fun registerGroupRecyclerView() = with(binding.groupRecyclerView) {
        adapter = GroupAdpater(context) { post ->

        }.apply {
            groupAdpater = this
        }
    }

    private fun registerTagRecyclerView() = with(binding.tagRecyclerView) {
        adapter = SearchTagAdapter(tagViewModel::removeTag).apply {
            searchTagAdapter = this
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
            search()
        }
    }

    private fun registerWriteButton() = with(binding.writeImageButton) {
        if (mode == null) {
            visibility = View.GONE
            return@with
        }
        setOnClickListener {
            postViewModel.createPost(listOf("태그1", "태그2"))
            /*val intent = Intent(context, WriteActivity::class.java)
            startActivity(intent)*/
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
                    is TagEvent.SetTag -> searchTagAdapter.setTags(event.tags)
                    is TagEvent.AddTag -> searchTagAdapter.addTag(event.tag)
                    is TagEvent.RemoveTag -> searchTagAdapter.removeTag(event.position)
                }
            }
        }
    }

    private fun search() {
        groupViewModel.onSearch(mode, tagViewModel.tags)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}