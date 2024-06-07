package kr.cosine.groupfinder.presentation.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.cosine.groupfinder.databinding.FragmentSearchBinding
import kr.cosine.groupfinder.presentation.view.common.model.TagViewModel
import kr.cosine.groupfinder.presentation.view.list.adapter.decoration.GroupTagItemDecoration
import kr.cosine.groupfinder.presentation.view.search.adapter.SearchAdapter

class SearchFragment() : BottomSheetDialogFragment() {

    //Binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    //RecyclerViewAdapter
    private lateinit var micAdapter: SearchAdapter
    private lateinit var styleAdapter: SearchAdapter

    //ViewModel
    private val tagViewModel by activityViewModels<TagViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMicRecyclerView()
        setupStyleRecyclerView()
    }

    private fun setupMicRecyclerView() {
        micAdapter = SearchAdapter(
            micTags,
            onItemClick = { _, tag ->
                if (!tagViewModel.isTagged(tag)) {
                    tagViewModel.addTag(tag)
                } else {
                    tagViewModel.removeTag(tag)
                }
            }
        )
        binding.tagMicRecyClerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = micAdapter
            addItemDecoration(GroupTagItemDecoration)
        }
    }

    private fun setupStyleRecyclerView() {
        styleAdapter = SearchAdapter(
            styleTags,
            onItemClick = { _, tag ->
                if (!tagViewModel.isTagged(tag)) {
                    tagViewModel.addTag(tag)
                } else {
                    tagViewModel.removeTag(tag)
                }
            }
        )

        binding.tagStyleRecyClerView.apply {
            layoutManager = StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)
            adapter = styleAdapter
            addItemDecoration(GroupTagItemDecoration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}