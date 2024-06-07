package kr.cosine.groupfinder.presentation.view.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemTagWithRemoveBinding

class SearchAdapter(
    private val items: List<String>,
    private val onItemClick: (position: Int, tag: String) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(binding: ItemTagWithRemoveBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tag = binding.tagTextView
//        val removeButton = binding.tagRemoveImageButton

        init {
            binding.tagTextView.apply {
                setOnClickListener {
                    onItemClick(bindingAdapterPosition, text.toString())
                }
            }
        }
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
    }
}

