package kr.cosine.groupfinder.presentation.view.list.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.registry.LocalAccountRegistry
import kr.cosine.groupfinder.databinding.ItemGroupBinding
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.list.adapter.decoration.GroupTagItemDecoration
import kr.cosine.groupfinder.presentation.view.list.adapter.listener.TagScrollListener
import kr.cosine.groupfinder.presentation.view.list.state.item.PostItem
import kr.cosine.groupfinder.presentation.view.list.state.item.extension.joinedPeopleCount
import kr.cosine.groupfinder.presentation.view.list.state.item.extension.tageedNickname
import kr.cosine.groupfinder.presentation.view.list.state.item.extension.totalPeopleCount
import kr.cosine.groupfinder.util.TimeUtil

class GroupAdpater(
    private val context: Context,
    private val onItemClick: (PostItem) -> Unit = {}
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

        fun bind(post: PostItem) = with(binding) {
            groupTitleTextView.text = post.title
            groupIdTextView.text = post.owner.tageedNickname
            val tags = post.tags
            val isMaxTag = tags.size >= MAX_TAG
            if (isMaxTag) {
                noticeMoreTagImageView.visibility = View.VISIBLE
            }
            groupTagRecyclerView.apply {
                val groupTagAdapter = GroupTagAdapter(tags)
                adapter = groupTagAdapter
                removeItemDecoration(GroupTagItemDecoration)
                addItemDecoration(GroupTagItemDecoration)
                addOnScrollListener(TagScrollListener(noticeMoreTagImageView, isMaxTag))
            }
            val laneMap = post.laneMap
            val joinedPeopleCount = post.joinedPeopleCount
            val totalPeopleCount = post.totalPeopleCount
            val isMaxGroup = joinedPeopleCount == totalPeopleCount
            groupLaneRecyclerView.adapter = GroupLaneAdapter(laneMap, isMaxGroup)
            groupPeopleTextView.text = context.getString(
                R.string.group_people_format,
                joinedPeopleCount,
                totalPeopleCount
            )
            groupTimeTextView.text = context.getString(
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

    private val posts = mutableListOf<PostItem>()

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
    fun setPosts(posts: List<PostItem>) {
        this.posts.apply {
            clear()
            addAll(posts)
        }
        notifyDataSetChanged()
    }

    fun clearPosts() {
        setPosts(emptyList())
    }

    private companion object {
        const val MAX_TAG = 7
    }
}