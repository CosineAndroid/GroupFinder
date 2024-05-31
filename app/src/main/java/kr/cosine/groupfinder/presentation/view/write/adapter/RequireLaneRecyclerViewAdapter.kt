package kr.cosine.groupfinder.presentation.view.write.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.withContext
import kr.cosine.groupfinder.databinding.ItemSelectlaneBinding
import kr.cosine.groupfinder.presentation.view.write.LaneSpinnerItem
import kr.cosine.groupfinder.presentation.view.write.WriteActivity

//스피너 아이템이 있고, 리싸이클러뷰를 표시하기 위한 아이템 근데 이 두개가 같은 리스트를 쓰면 안 되고 각자
//리스트를 가지고 있어야한다

//불특정한 리스트를 보여줄때 recycler가 특화되어 있는데, 4개의 고정값을 가지고 있는 리스트를 보여주는건 view만
//그리면 돼서 굳이 쓸 필요는 없다

class RequireLaneRecyclerViewAdapter(
    private val lanes: MutableList<String>,
) : RecyclerView.Adapter<RequireLaneRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemSelectlaneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lane = lanes[position]
        holder.bind(holder.itemView.context, lane)
    }

    override fun getItemCount(): Int {
        return lanes.size
    }

    fun removeLane(position: Int) {
       if (position < lanes.size ) {
            lanes.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, lanes.size)
        }
    }

    //보여주고 중복된 값이라고 알려주는게 낫다
    fun addLane(newLane: String) {
        lanes.add(newLane)
        notifyItemInserted(lanes.size - 1)
    }


    class ViewHolder(
        private val binding: ItemSelectlaneBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.removeImageView.setOnClickListener {
                (binding.root.context as WriteActivity).requireLaneRecyclerViewAdapter.removeLane(bindingAdapterPosition)
            }
        }
        fun bind(context: Context, lane: String) {
            val spinner = binding.requireLaneSpinner
            val adapter = RequireLaneSpinnerAdapter(context, LaneSpinnerItem.laneItems)
            spinner.adapter = adapter
        }
    }
}