package kr.cosine.groupfinder.presentation.view.detail

import android.health.connect.datatypes.units.Power
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.databinding.ActivityDetailSelectLaneBinding
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.TestGlobalUserData.HOST
import kr.cosine.groupfinder.enums.TestGlobalUserData.PARTICIPANT
import kr.cosine.groupfinder.enums.TestGlobalUserData.userID

class DetailLaneAdapter(): RecyclerView.Adapter<DetailLaneAdapter.Holder>() {
    private var laneMap: Map<Lane, String?> = emptyMap()
    private var power: Int = 0

    interface ItemClick {
        fun onClick(view: View, lane: Lane)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ActivityDetailSelectLaneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return laneMap.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val entry = laneMap.entries.toList()[position]
        holder.itemView.setOnClickListener {
            if (entry.value == null) {
                itemClick?.onClick(it, entry.key)
            }
        }
        holder.bind(entry.key,entry.value, power)

    }

    class Holder(private val binding: ActivityDetailSelectLaneBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            private val laneIcons = mapOf(
                Lane.TOP to Pair(R.drawable.ic_lane_top, R.drawable.ic_lane_empty_top),
                Lane.JUNGLE to Pair(R.drawable.ic_lane_jungle, R.drawable.ic_lane_empty_jungle),
                Lane.MID to Pair(R.drawable.ic_lane_mid, R.drawable.ic_lane_empty_mid),
                Lane.AD to Pair(R.drawable.ic_lane_ad, R.drawable.ic_lane_empty_ad),
                Lane.SUPPORT to Pair(R.drawable.ic_lane_spt, R.drawable.ic_lane_empty_spt)
            )
        }

        fun bind(lane: Lane, userName: String?, power: Int) {
            val (icon, emptyIcon) = laneIcons[lane] ?: return
            binding.selectLaneImageView.setImageResource(if (userName != null) icon else emptyIcon)
            binding.selectIdTextView.text = userName ?: "없음"

            binding.selectExitImageView.visibility = when(power) {
                HOST -> View.VISIBLE
                PARTICIPANT -> if(userName == userID) View.VISIBLE else View.INVISIBLE
                else -> View.INVISIBLE
            }
        }
    }

    fun laneUpdate(item: Map<Lane,String?>) {
        this.laneMap = item
    }

    fun powerUpdate(item: Int) {
        this.power = item
        notifyDataSetChanged()
    }

}