package kr.cosine.groupfinder.presentation.view.tag.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.databinding.FragmentTagBottomSheetBinding
import kr.cosine.groupfinder.presentation.view.common.flexbox.decoration.FlexboxItemDecoration
import kr.cosine.groupfinder.presentation.view.common.flexbox.manager.FlexboxLayoutManager
import kr.cosine.groupfinder.presentation.view.tag.adapter.TagAdapter
import kr.cosine.groupfinder.presentation.view.tag.model.TagViewModel

class TagBottomSheetFragment : BottomSheetDialogFragment() {

    //Binding
    private var _binding: FragmentTagBottomSheetBinding? = null
    private val binding get() = _binding!!

    //ViewModel
    private val tagViewModel by activityViewModels<TagViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTagBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(binding.tagMicRecyclerView, micTags)
        setupRecyclerView(binding.tagStyleRecyclerView, styleTags)
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        tags: MutableList<String>
    ) = with(recyclerView) {
        adapter = TagAdapter(tags, this@TagBottomSheetFragment::onItemClick)
        layoutManager = FlexboxLayoutManager(context)
        val flexboxItemDecoration = FlexboxItemDecoration(context)
        addItemDecoration(flexboxItemDecoration)
    }

    private fun onItemClick(tag: String) {
        if (!tagViewModel.isTagged(tag)) {
            tagViewModel.addTag(tag)
        } else {
            tagViewModel.removeTag(tag)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val micTags = mutableListOf("마이크X", "마이크O", "상관없음")
        private val styleTags = mutableListOf("빡겜", "즐겜", "욕X", "듣톡", "채팅X")

        private const val TAG = "TagBottomSheetFragment"

        fun show(fragmentManager: FragmentManager) {
            val tagBottomSheetFragment = TagBottomSheetFragment()
            tagBottomSheetFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundCornerBottomSheetDialogTheme)
            tagBottomSheetFragment.show(fragmentManager, TAG)
        }
    }
}