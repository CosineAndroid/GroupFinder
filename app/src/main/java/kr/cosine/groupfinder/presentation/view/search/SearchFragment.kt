package kr.cosine.groupfinder.presentation.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kr.cosine.groupfinder.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {

        //클릭한 태그 observe하는 코드

        binding.apply {
            tagMicRecyClerView.adapter = SearchRecyclerView(micTags)
            tagStyleRecyClerView.adapter = SearchRecyclerView(styleTags)

            tagMicRecyClerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            tagStyleRecyClerView.layoutManager =
                StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL)
        }

        //이미 클릭한 태그는 누르면 삭제하고 새로운 태그는 누르면 선택하기
    }
}