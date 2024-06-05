package kr.cosine.groupfinder.presentation.view.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemGroupLaneBinding
import kr.cosine.groupfinder.enums.Lane
import java.util.UUID

class GroupLaneAdapter(
    private val laneMap: Map<Lane, UUID?>,
    private val isMaxGroup: Boolean
) : RecyclerView.Adapter<GroupLaneAdapter.GroupLaneViewHolder>() {

    inner class GroupLaneViewHolder(
        private val binding: ItemGroupLaneBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lane: Lane, owner: UUID?) {
            val drawableId = lane.getDrawableIdByOwner(!isMaxGroup && owner != null)
            binding.laneImageView.setImageResource(drawableId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupLaneViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGroupLaneBinding.inflate(layoutInflater, parent, false)
        return GroupLaneViewHolder(binding)
    }

    override fun getItemCount(): Int = laneMap.size

    override fun onBindViewHolder(holder: GroupLaneViewHolder, position: Int) {
        val laneEntry = laneMap.entries.toList()[position]
        holder.bind(laneEntry.key, laneEntry.value)
    }
}