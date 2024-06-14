package kr.cosine.groupfinder.presentation.view.write.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.databinding.ItemSelectlaneBinding
import kr.cosine.groupfinder.presentation.view.write.LaneSpinnerItem
import kr.cosine.groupfinder.presentation.view.write.RequireLane

class RequireLaneRecyclerViewAdapter(
    private val lanes: MutableList<RequireLane>,
    private val onLaneCountChanged: () -> Unit
) : RecyclerView.Adapter<RequireLaneRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectlaneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(lanes[position],position)
    }

    override fun getItemCount(): Int {
        return lanes.size
    }

    inner class ViewHolder(
        private val binding: ItemSelectlaneBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.removeImageView.setOnClickListener {
                removeLane(bindingAdapterPosition)

            }
        }

        fun bind(lane: RequireLane, requireLanePosition: Int) {
            val spinner = binding.requireLaneSpinner
            val adapter = RequireLaneSpinnerAdapter(binding.root.context, LaneSpinnerItem.laneItems)
            spinner.adapter = adapter

            binding.tagBackgroundCardView.visibility = if (requireLanePosition > 0) View.VISIBLE else View.INVISIBLE

            spinner.setSelection(LaneSpinnerItem.laneItems.indexOfFirst { it.lane == lane.lane })
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedLane = LaneSpinnerItem.laneItems[position].lane
                    lane.lane = selectedLane
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    fun getLanes(): List<RequireLane> {
        return lanes.toList()
    }

    fun removeLane(position: Int) {
        if (lanes.size > 1 && position < lanes.size) {
            lanes.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, lanes.size)
            updateAddLaneButtonVisibility()
            onLaneCountChanged()
        }
    }

    private fun updateAddLaneButtonVisibility() {
        if (lanes.size < 4) {
            onLaneCountChanged()
        }
    }

    fun addLane(newLane: String) {
        val newRequireLane = RequireLane(newLane)
        lanes.add(newRequireLane)
        notifyItemInserted(lanes.size - 1)
        updateAddLaneButtonVisibility()
        onLaneCountChanged()
    }
}