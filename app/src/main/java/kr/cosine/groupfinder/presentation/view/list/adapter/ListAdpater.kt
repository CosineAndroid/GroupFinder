package kr.cosine.groupfinder.presentation.view.list.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.data.model.PostModel
import kr.cosine.groupfinder.data.model.PostModel.Companion.getJoinedPeopleCount
import kr.cosine.groupfinder.data.model.PostModel.Companion.getLaneMap
import kr.cosine.groupfinder.data.model.PostModel.Companion.getTotalPeopleCount
import kr.cosine.groupfinder.databinding.ItemGroupBinding
import kr.cosine.groupfinder.enums.Lane
import kr.cosine.groupfinder.enums.Mode
import kr.cosine.groupfinder.presentation.view.list.adapter.decoration.TagItemDecoration
import kr.cosine.groupfinder.presentation.view.list.adapter.listener.TagScrollListener
import kr.cosine.groupfinder.util.TimeUtil

class ListAdpater(
    private val context: Context,
    private val onItemClick: (PostModel) -> Unit = {}
) : RecyclerView.Adapter<ListAdpater.GroupViewHolder>() {

    inner class GroupViewHolder(
        private val binding: ItemGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val root = binding.root

        init {
            root.setOnClickListener {
                val postModel = posts[bindingAdapterPosition]
                onItemClick(postModel)
            }
        }

        fun bind(postModel: PostModel) = with(binding) {
            groupTitleTextView.text = postModel.title
            groupIdTextView.text = postModel.id
            val tags = postModel.tags
            val isMaxTag = tags.size >= MAX_TAG
            if (isMaxTag) {
                noticeMoreTagImageView.visibility = View.VISIBLE
            }
            groupTagRecyclerView.apply {
                val tagAdpater = TagAdpater(tags)
                adapter = tagAdpater
                addItemDecoration(tagItemDecoration)
                addOnScrollListener(TagScrollListener(noticeMoreTagImageView, isMaxTag))
            }
            val laneMap = postModel.getLaneMap()
            val joinedPeopleCount =  postModel.getJoinedPeopleCount()
            val totalPeopleCount = postModel.getTotalPeopleCount()
            val isMaxGroup = joinedPeopleCount == totalPeopleCount
            groupLaneRecyclerView.adapter = LaneAdpater(laneMap, isMaxGroup)
            groupPeopleTextView.text = context.getString(
                R.string.group_people_format,
                joinedPeopleCount,
                totalPeopleCount
            )
            groupTimeTextView.text = context.getString(
                R.string.group_time_format,
                TimeUtil.getFormattedTime(postModel.time)
            )
            if (isMaxGroup) {
                root.background = AppCompatResources.getDrawable(
                    context,
                    R.drawable.list_full_group_background
                )
            }
        }
    }

    private val posts = mutableListOf<PostModel>(
        // PostModel(Mode.NORMAL, "5인큐 구함", "반갑습니다", "페이커#KR1", listOf("빡겜", "즐겜", "하이", "가나", "다라", "마바", "사아", "자차", "카타", "파하"), mapOf(Lane.TOP to null, Lane.SUPPORT to null, Lane.AD to "구마유시#KR1", Lane.JUNGLE to null, Lane.MID to null, Lane.MID to "페이커#KR1")),
        // PostModel(Mode.NORMAL, "바텀 듀오 할 사람", "ㅎㅇ", "구마유시#KR1", listOf("즐겜", "마이크X"), mapOf(Lane.AD to "구마유시#KR1", Lane.SUPPORT to null)),
        // PostModel(Mode.NORMAL, "정글 듀오 구함", "안녕하세요", "제우스#KR1", listOf("욕X"), mapOf(Lane.JUNGLE to null, Lane.TOP to "제우스#KR1")),
        // PostModel(Mode.NORMAL, "3인큐 구함", "안녕하세요", "케리아#KR1", listOf("욕X"), mapOf(Lane.SUPPORT to "테스트#KR1", Lane.JUNGLE to null, Lane.TOP to "제우스#KR1")),
        // PostModel(Mode.NORMAL, "3인큐 구함!!", "안녕하세요", "오너#KR1", listOf("욕X"), mapOf(Lane.MID to "페이커#KR1", Lane.JUNGLE to "오너#KR1", Lane.SUPPORT to "케리아#KR1"))
    )

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
    fun setPosts(posts: List<PostModel>) {
        this.posts.apply {
            clear()
            addAll(posts)
        }
        notifyDataSetChanged()
    }

    private companion object {
        val tagItemDecoration = TagItemDecoration(bottom = 0)
        const val MAX_TAG = 7
    }
}