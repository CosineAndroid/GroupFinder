package kr.cosine.groupfinder.presentation.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.cosine.groupfinder.databinding.FragmentSearchBinding
import kr.cosine.groupfinder.presentation.view.list.adapter.SearchTagAdpater
import kr.cosine.groupfinder.presentation.view.list.model.GroupViewModel
import kr.cosine.groupfinder.presentation.view.list.model.TagViewModel
import kr.cosine.groupfinder.presentation.view.search.adapter.SearchAdapter


class SearchFragment() : BottomSheetDialogFragment() {

    //Binding
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    //RecyclerViewAdapter
    private lateinit var micAdapter: SearchAdapter
    private lateinit var styleAdapter: SearchAdapter
    private lateinit var searchTagAdpater: SearchTagAdpater

    //ViewModel
    private val groupViewModel by viewModels<GroupViewModel>()
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

//        micAdapter = SearchAdapter(micTags)
//        styleAdapter = SearchAdapter(styleTags)
//
//        binding.apply {
//            tagMicRecyClerView.layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//            tagStyleRecyClerView.layoutManager =
//                StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)
//
//            tagMicRecyClerView.adapter = micAdapter
//            tagStyleRecyClerView.adapter = styleAdapter
//        }

//        micAdapter.itemClick = object : SearchAdapter.ItemClick {
//            override fun onItemClick(id: String) {
//                if (!Tags.selectedTagList.contains(id)) {
//                    Tags.addTag("id")
//                    Log.d("tag", "${Tags.selectedTagList}")
//                }
//
//            }
//        }
    }

    private fun registerMicTagRecyclerView() = with(binding.tagMicRecyClerView) {
        adapter = SearchAdapter(micTags, tagViewModel::addTag).apply {
            micAdapter = this
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}