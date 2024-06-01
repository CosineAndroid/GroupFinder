package kr.cosine.groupfinder.presentation.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemTagWithRemoveBinding

class SearchAdapter(
    private val items: List<String>)
    : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    interface ItemClick {
        fun onItemClick(id: CharSequence)
    }

    var itemClick: ItemClick? = null

    inner class SearchViewHolder(binding: ItemTagWithRemoveBinding): RecyclerView.ViewHolder(binding.root){
        val tag = binding.tagTextView
        val removeButton = binding.tagRemoveImageButton

        init {
            tag.setOnClickListener{
                val position = adapterPosition
                val tagText = tag.text
//                if(position!=RecyclerView.NO_POSITION){
//                    onAddClick(items[position])
//                }
                itemClick?.onItemClick(tagText)

//                val clickedText = tag.text.toString() // 클릭한 아이템의 텍스트 가져오기
//                if (tagList.contains(clickedText)) {
//                    // 처리할 작업 수행
//                }
            }

//            removeButton.setOnClickListener{
//                val position = adapterPosition
//                if(position!=RecyclerView.NO_POSITION){
//                    onDeleteClick(items[position])
//                }
//            }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemTagWithRemoveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.tag.text = items[position]
        holder.itemView.setOnClickListener{
            itemClick?.onItemClick(holder.tag.text)
        }
    }
}

