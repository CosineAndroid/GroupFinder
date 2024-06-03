package kr.cosine.groupfinder.presentation.view.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemTagBinding

class DetailCategoryAdapter():RecyclerView.Adapter<DetailCategoryAdapter.Holder>() {
    var categories = listOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentCategory = categories[position]
        holder.bind(currentCategory)
    }

    class Holder(private val binding: ItemTagBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(category: String) {
            binding.tagTextView.text = category
        }
    }

    fun categoriesUpdate(item: List<String>) {
        this.categories = item
        notifyDataSetChanged()
    }
}