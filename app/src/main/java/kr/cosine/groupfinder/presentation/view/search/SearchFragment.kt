package kr.cosine.groupfinder.presentation.view.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kr.cosine.groupfinder.databinding.FragmentSearchBinding
import kr.cosine.groupfinder.presentation.view.search.adapter.SearchAdapter

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var micAdapter: SearchAdapter
    private lateinit var styleAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        micAdapter = SearchAdapter(micTags)
        styleAdapter = SearchAdapter(styleTags)

        binding.apply {
            tagMicRecyClerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            tagStyleRecyClerView.layoutManager =
                StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL)

            tagMicRecyClerView.adapter = micAdapter
            tagStyleRecyClerView.adapter = styleAdapter
        }

        micAdapter.itemClick = object : SearchAdapter.ItemClick {
            override fun onItemClick(id: String) {
                if (!Tags.selectedTagList.contains(id)) {
                    Tags.addTag("id")
                    Log.d("tag", "${Tags.selectedTagList}")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}