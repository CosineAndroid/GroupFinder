package kr.cosine.groupfinder.presentation.view.list.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.databinding.ItemGroupBinding
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.list.adapter.decoration.GroupTagItemDecoration
import kr.cosine.groupfinder.presentation.view.list.adapter.listener.TagScrollListener
import kr.cosine.groupfinder.presentation.view.list.state.item.GroupItem
import kr.cosine.groupfinder.presentation.view.list.state.item.extension.isJoinedPeople
import kr.cosine.groupfinder.presentation.view.list.state.item.extension.joinedPeopleCount
import kr.cosine.groupfinder.presentation.view.list.state.item.extension.tageedNickname
import kr.cosine.groupfinder.presentation.view.list.state.item.extension.totalPeopleCount
import kr.cosine.groupfinder.util.TimeUtil

class GroupAdpater(
    private val context: Context,
    private val onItemClick: (GroupItem) -> Unit = {}
) : RecyclerView.Adapter<GroupAdpater.GroupViewHolder>() {

    inner class GroupViewHolder(
        private val binding: ItemGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListenerWithCooldown(Interval.OPEN_SCREEN) {
                val post = posts[bindingAdapterPosition]
                onItemClick(post)
            }
        }

        fun bind(post: GroupItem) = with(binding) {
            val joinedPeopleCount = post.joinedPeopleCount
            val totalPeopleCount = post.totalPeopleCount

            val isMaxGroup = !post.isJoinedPeople(LocalAccountRegistry.uniqueId) &&
                    joinedPeopleCount == totalPeopleCount

            fun TextView.applyColor(color: Int = R.color.group_full_text): TextView {
                if (isMaxGroup) {
                    val colorStateList = context.getColorStateList(color)
                    setTextColor(colorStateList)
                }
                return this
            }

            groupTitleTextView.applyColor(R.color.group_full_title_text).text = post.title
            groupTaggedNicknameTextView.applyColor().text = post.owner.tageedNickname
            val tags = post.tags
            val isMaxTag = tags.size >= MAX_TAG
            if (isMaxTag) {
                noticeMoreTagImageView.visibility = View.VISIBLE
            }
            groupTagRecyclerView.apply {
                val groupTagAdapter = GroupTagAdapter(tags, isMaxGroup)
                adapter = groupTagAdapter
                removeItemDecoration(GroupTagItemDecoration)
                addItemDecoration(GroupTagItemDecoration)
                addOnScrollListener(TagScrollListener(noticeMoreTagImageView, isMaxTag))
            }
            val laneMap = post.laneMap
            groupLaneRecyclerView.adapter = GroupLaneAdapter(laneMap, isMaxGroup)
            groupPeopleTextView.applyColor().text = context.getString(
                R.string.group_people_format,
                joinedPeopleCount,
                totalPeopleCount
            )
            groupTimeTextView.applyColor().text = context.getString(
                R.string.group_time_format,
                TimeUtil.getFormattedTime(post.time)
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

    private val posts = mutableListOf<GroupItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemGroupBinding.inflate(layoutInflater, parent, false)
        return GroupViewHolder(binding)
    }

    override fun getItemCount(): Int = posts.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPosts(posts: List<GroupItem>) {
        this.posts.apply {
            clear()
            addAll(posts)
        }
        notifyDataSetChanged()
    }

    fun setPost(post: GroupItem) {
        setPosts(listOf(post))
    }

    fun clearPosts() {
        setPosts(emptyList())
    }

    private companion object {
        const val MAX_TAG = 7
    }
}