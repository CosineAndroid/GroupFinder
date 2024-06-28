package kr.cosine.groupfinder.presentation.view.tag.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemTagBinding
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown

class TagAdapter(
    private val tags: MutableList<String> = mutableListOf(),
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {

    inner class TagViewHolder(
        private val binding: ItemTagBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListenerWithCooldown(Interval.CLICK_TAG) {
                    onItemClick(tagTextView.text.toString())
                }
            }
        }

        fun bind(tag: String) {
            binding.tagTextView.text = tag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTagBinding.inflate(layoutInflater, parent, false)
        return TagViewHolder(binding)
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
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