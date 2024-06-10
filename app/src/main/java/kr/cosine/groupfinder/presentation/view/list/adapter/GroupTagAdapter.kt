package kr.cosine.groupfinder.presentation.view.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.databinding.ItemTagBinding

class GroupTagAdapter(
    private val tags: List<String>,
    private val isMaxGroup: Boolean = false
) : RecyclerView.Adapter<GroupTagAdapter.GroupTagViewHolder>() {

    inner class GroupTagViewHolder(
        private val binding: ItemTagBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            if (isMaxGroup) {
                binding.root.setBackgroundResource(R.drawable.tag_full_group_background)
            }
        }

        fun bind(tag: String) {
            binding.tagTextView.text = tag
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupTagViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTagBinding.inflate(layoutInflater, parent, false)
        return GroupTagViewHolder(binding)
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: GroupTagViewHolder, position: Int) {
        holder.bind(tags[position])
    }
}