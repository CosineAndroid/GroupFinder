package kr.cosine.groupfinder.presentation.view.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry.uniqueId
import kr.cosine.groupfinder.databinding.ActivityDetailSelectLaneBinding
import kr.cosine.groupfinder.domain.model.GroupOwnerEntity
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.TestGlobalUserData.HOST
import kr.cosine.groupfinder.enums.TestGlobalUserData.NONE
import kr.cosine.groupfinder.enums.TestGlobalUserData.PARTICIPANT
import java.util.UUID

class DetailLaneAdapter : RecyclerView.Adapter<DetailLaneAdapter.Holder>() {
    private var laneMap: Map<Lane, GroupOwnerEntity?> = emptyMap()
    private var power: Int = 0

    interface ItemClick {
        fun onClick(view: View, lane: Lane)
        fun onExitClick(view: View, lane: Lane, userUUID: UUID)
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
        holder.bind(entry.key, entry.value, power, itemClick)
    }

    class Holder(
        private val binding: ActivityDetailSelectLaneBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lane: Lane, userInfo: GroupOwnerEntity?, power: Int, itemClick: ItemClick?) {
            val (icon, emptyIcon) = lane.drawableId to lane.emptyDrawableId
            binding.selectLaneImageView.setImageResource(if (userInfo != null) icon else emptyIcon)
            val text = if (userInfo != null) {
                "${userInfo.nickname}#${userInfo.tag}"
            } else {
                "없음"
            }
            binding.selectIdTextView.text = text

            val isExitVisible = when(power) {
                HOST -> userInfo?.uniqueId != uniqueId
                PARTICIPANT -> userInfo?.uniqueId == uniqueId
                else -> false
            }

            binding.selectExitImageView.visibility = if (isExitVisible && (userInfo != null)) View.VISIBLE else View.INVISIBLE

            binding.selectExitImageView.setOnClickListener {
                if (isExitVisible) {
                    userInfo?.let { userId ->
                        itemClick?.onExitClick(it, lane, userInfo.uniqueId)
                    }
                }
            }

            binding.root.setOnClickListener {
                if (power == NONE && userInfo == null) {
                    itemClick?.onClick(it, lane)
                }
            }
        }
    }

    fun laneUpdate(item: Map<Lane, GroupOwnerEntity?>) {
        this.laneMap = item
    }

    fun powerUpdate(item: Int) {
        this.power = item
        notifyDataSetChanged()
    }
}

