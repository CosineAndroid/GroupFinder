package kr.cosine.groupfinder.presentation.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemTagBinding

class SearchRecyclerView(private val items: List<String>)
    : RecyclerView.Adapter<SearchRecyclerView.SearchViewHolder>() {
    interface ItemClick {
        fun itemClick(id: String)
    }

    var itemClick: ItemClick? = null

    inner class SearchViewHolder(binding: ItemTagBinding): RecyclerView.ViewHolder(binding.root){
        val tag = binding.tagTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.tag.text = items[position]
    }
}