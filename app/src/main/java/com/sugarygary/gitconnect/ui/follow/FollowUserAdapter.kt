package com.sugarygary.gitconnect.ui.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sugarygary.gitconnect.data.model.User
import com.sugarygary.gitconnect.databinding.UserItemBinding
import com.sugarygary.gitconnect.utils.glide

class FollowUserAdapter(
    private val identifier: String,
    private val onClick: (ImageView, String) -> Unit
) :
    ListAdapter<User, FollowUserAdapter.FollowUserViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowUserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowUserViewHolder, position: Int) {
        val user = getItem(position)
        if (user.avatarUrl != null) {
            holder.binding.ivProfile.glide(user.avatarUrl)
        }
        holder.binding.tvUsername.text = user.login
        holder.binding.root.setOnClickListener {
            onClick(holder.binding.ivProfile, user.login)
        }
        //beri attribute transitionName agar dikenali oleh MaterialContainerTransform
        holder.binding.ivProfile.transitionName = "image-${user.login}-${identifier}"
    }

    inner class FollowUserViewHolder(val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }
}