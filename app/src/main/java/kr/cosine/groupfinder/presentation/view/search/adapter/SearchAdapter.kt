package kr.cosine.groupfinder.presentation.view.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemTagWithRemoveBinding

class SearchAdapter(
    private val items: List<String>
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    interface ItemClick {
        fun onItemClick(id: String)
    }

    var itemClick: ItemClick? = null

    inner class SearchViewHolder(binding: ItemTagWithRemoveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tag = binding.tagTextView
        val removeButton = binding.tagRemoveImageButton
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding =
            ItemTagWithRemoveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.tag.text = items[position]
        holder.itemView.setOnClickListener {
            itemClick?.onItemClick(holder.tag.text as String)
        }
    }
}

//도메인 영역에 태그를 가져오는 usecase를 만들어서 두개의 화면에 뷰모델에서 태그를 가져오는 유즈케이스를 사용

