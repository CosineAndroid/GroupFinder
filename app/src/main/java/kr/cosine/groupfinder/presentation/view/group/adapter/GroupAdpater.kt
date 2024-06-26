package kr.cosine.groupfinder.presentation.view.group.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.databinding.ItemGroupBinding
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.group.adapter.decoration.GroupTagItemDecoration
import kr.cosine.groupfinder.presentation.view.group.adapter.listener.TagScrollListener
import kr.cosine.groupfinder.presentation.view.group.state.item.GroupItem
import kr.cosine.groupfinder.presentation.view.group.state.item.extension.isJoinedPeople
import kr.cosine.groupfinder.presentation.view.group.state.item.extension.joinedPeopleCount
import kr.cosine.groupfinder.presentation.view.group.state.item.extension.totalPeopleCount
import kr.cosine.groupfinder.util.TimeUtil

class GroupAdpater(
    private val onItemClick: (GroupItem) -> Unit = {}
) : RecyclerView.Adapter<GroupAdpater.GroupViewHolder>() {

    inner class GroupViewHolder(
        private val binding: ItemGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        init {
            binding.root.setOnClickListenerWithCooldown(Interval.OPEN_SCREEN) {
                val group = groups[bindingAdapterPosition]
                onItemClick(group)
            }
        }

        fun bind(group: GroupItem) = with(binding) {
            val joinedPeopleCount = group.joinedPeopleCount
            val totalPeopleCount = group.totalPeopleCount

            val isMaxGroup = !group.isJoinedPeople(LocalAccountRegistry.uniqueId) &&
                    joinedPeopleCount == totalPeopleCount

            fun TextView.applyColor(defaultColor: Int, fullColor: Int): TextView {
                val color = if (isMaxGroup) fullColor else defaultColor
                val colorStateList = context.getColorStateList(color)
                setTextColor(colorStateList)
                return this
            }

            groupTitleTextView.applyColor(
                R.color.group_default_title,
                R.color.group_full_title_text
            ).text = group.title

            val owner = group.owner
            groupTaggedNicknameTextView.applyColor(
                R.color.group_default_tagged_nickname,
                R.color.group_full_text
            ).text = context.getString(
                R.string.tagged_nickname_format,
                owner.nickname,
                owner.tag
            )

            val tags = group.tags
            val isMaxTag = tags.size >= MAX_TAG
            if (isMaxTag) {
                noticeMoreTagImageView.visibility = View.VISIBLE
            }
            groupTagRecyclerView.apply {
                adapter = GroupTagAdapter(tags, isMaxGroup)
                removeItemDecoration(GroupTagItemDecoration)
                addItemDecoration(GroupTagItemDecoration)
                addOnScrollListener(TagScrollListener(noticeMoreTagImageView, isMaxTag))
            }
            val laneMap = group.laneMap
            groupLaneRecyclerView.adapter = GroupLaneAdapter(laneMap, isMaxGroup)

            groupPeopleTextView.applyColor(
                R.color.group_default_people_text,
                R.color.group_full_text
            ).text = context.getString(
                R.string.group_people_format,
                joinedPeopleCount,
                totalPeopleCount
            )

            groupTimeTextView.applyColor(
                R.color.group_default_time_text,
                R.color.group_full_text
            ).text = context.getString(
                R.string.group_time_format,
                TimeUtil.getFormattedTime(group.time)
            )

            root.background = AppCompatResources.getDrawable(
                context,
                when {
                    laneMap.values.contains(LocalAccountRegistry.uniqueId) -> R.drawable.group_join_background
                    isMaxGroup -> R.drawable.group_full_background
                    else -> R.drawable.group_default_background
                }
            )
        }
    }

    private val groups = mutableListOf<GroupItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGroupBinding.inflate(layoutInflater, parent, false)
        return GroupViewHolder(binding)
    }

    override fun getItemCount(): Int = groups.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setGroups(groups: List<GroupItem>) {
        this.groups.apply {
            clear()
            addAll(groups)
        }
        notifyDataSetChanged()
    }

    fun setGroup(group: GroupItem) {
        setGroups(listOf(group))
    }

    fun clearGroups() {
        setGroups(emptyList())
    }

    private companion object {
        const val MAX_TAG = 7
    }
}