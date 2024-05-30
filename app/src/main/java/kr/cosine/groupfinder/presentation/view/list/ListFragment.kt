package kr.cosine.groupfinder.presentation.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.cosine.groupfinder.databinding.FragmentListBinding
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.list.adapter.ListAdpater
import kr.cosine.groupfinder.presentation.view.list.model.ListViewModel
import kr.cosine.groupfinder.presentation.view.list.state.ListUiState

@AndroidEntryPoint
class ListFragment(
    private val mode: Mode
) : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding get() = _binding!!

    private val listViewModel by viewModels<ListViewModel>()

    private lateinit var listAdpater: ListAdpater

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerRecyclerView()
        registerViewModelEvent()
    }

    private fun registerRecyclerView() = with(binding.groupRecyclerView) {
        layoutManager = LinearLayoutManager(context)
        adapter = ListAdpater(context) { post ->

        }.apply {
            listAdpater = this
        }
    }

    private fun registerViewModelEvent() {
        listViewModel.onSearch(mode, emptyList())
        viewLifecycleOwner.lifecycleScope.launch {
            listViewModel.uiState.flowWithLifecycle(lifecycle).collectLatest { uiState ->
                if (uiState is ListUiState.Result) {
                    listAdpater.setPosts(uiState.posts)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}