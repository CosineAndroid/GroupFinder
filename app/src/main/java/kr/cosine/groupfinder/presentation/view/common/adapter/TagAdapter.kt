package kr.cosine.groupfinder.presentation.view.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemTagBinding

class TagAdapter(
    private val tags: MutableList<String> = mutableListOf(),
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<TagAdapter.SearchTagViewHolder>() {

    inner class SearchTagViewHolder(
        private val binding: ItemTagBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tagTextView.apply {
                setOnClickListener {
                    onItemClick(text.toString())
                }
            }
        }

        fun bind(tag: String) {
            binding.tagTextView.text = tag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchTagViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTagBinding.inflate(layoutInflater, parent, false)
        return SearchTagViewHolder(binding)
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: SearchTagViewHolder, position: Int) {
        holder.bind(tags[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTags(tags: List<String>) {
        this.tags.apply {
            clear()
            addAll(tags)
        }
        notifyDataSetChanged()
    }

    fun addTag(tag: String) {
        tags.add(tag)
        notifyItemChanged(tags.size)
    }

    fun removeTag(tag: String) {
        val index = tags.indexOf(tag)
        tags.remove(tag)
        notifyItemRemoved(index)
    }
}