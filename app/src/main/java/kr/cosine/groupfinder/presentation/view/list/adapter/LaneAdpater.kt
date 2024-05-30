package kr.cosine.groupfinder.presentation.view.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemGroupLaneBinding
import kr.cosine.groupfinder.enums.Lane

class LaneAdpater(
    private val laneMap: Map<Lane, String?>,
    private val isMaxGroup: Boolean
) : RecyclerView.Adapter<LaneAdpater.LaneViewHolder>() {

    inner class LaneViewHolder(
        private val binding: ItemGroupLaneBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lane: Lane, owner: String?) {
            val drawableId = lane.getDrawableIdByOwner(!isMaxGroup && owner != null)
            binding.laneImageView.setImageResource(drawableId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaneViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGroupLaneBinding.inflate(layoutInflater, parent, false)
        return LaneViewHolder(binding)
    }

    override fun getItemCount(): Int = laneMap.size

    override fun onBindViewHolder(holder: LaneViewHolder, position: Int) {
        val laneEntry = laneMap.entries.toList()[position]
        holder.bind(laneEntry.key, laneEntry.value)
    }
}