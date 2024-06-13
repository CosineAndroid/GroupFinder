package kr.cosine.groupfinder.presentation.view.profile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.cosine.groupfinder.R
import kr.cosine.groupfinder.databinding.ItemBlockUserBinding
import kr.cosine.groupfinder.presentation.view.common.data.Interval
import kr.cosine.groupfinder.presentation.view.common.extension.setOnClickListenerWithCooldown
import kr.cosine.groupfinder.presentation.view.profile.state.item.BlockUserItem

class BlockUserAdapter(
    private val onUnblockButtonClick: (BlockUserItem) -> Unit
) : RecyclerView.Adapter<BlockUserAdapter.BlockUserViewHolder>() {

    inner class BlockUserViewHolder(
        private val binding: ItemBlockUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.unblockImageButton.setOnClickListenerWithCooldown(Interval.CLICK_BUTTON) {
                val bindingAdapterPosition = bindingAdapterPosition
                val blockUser = blockUsers[bindingAdapterPosition]
                removeBlockUser(bindingAdapterPosition, blockUser)
                onUnblockButtonClick(blockUser)
            }
        }

        fun bind(blockUser: BlockUserItem) = with(binding.taggedNicknameTextView) {
            text = context.getString(
                R.string.tagged_nickname_format,
                blockUser.nickname,
                blockUser.tag
            )
        }
    }

    private val blockUsers = mutableListOf<BlockUserItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockUserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemBlockUserBinding.inflate(layoutInflater, parent, false)
        return BlockUserViewHolder(binding)
    }

    override fun getItemCount(): Int = blockUsers.size

    override fun onBindViewHolder(holder: BlockUserViewHolder, position: Int) {
        holder.bind(blockUsers[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBlockUsers(blockUsers: List<BlockUserItem>) {
        this.blockUsers.apply {
            clear()
            addAll(blockUsers)
        }
        notifyDataSetChanged()
    }

    private fun removeBlockUser(position: Int, blockUser: BlockUserItem) {
        blockUsers.remove(blockUser)
        notifyItemRemoved(position)
    }
}