package com.sugarygary.gitconnect.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sugarygary.gitconnect.data.model.User
import com.sugarygary.gitconnect.databinding.UserItemBinding
import com.sugarygary.gitconnect.utils.glide

class SearchUserAdapter(private val onClick: (ImageView, String) -> Unit) :
    ListAdapter<User, SearchUserAdapter.SearchUserViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val user = getItem(position)
        if (user.avatarUrl != null) {
            holder.binding.ivProfile.glide(user.avatarUrl)
        }
        holder.binding.tvUsername.text = user.login
        holder.binding.root.setOnClickListener {
            onClick.invoke(holder.binding.ivProfile, user.login)
        }
        //beri attribute transitionName agar dikenali oleh MaterialContainerTransform
        holder.binding.ivProfile.transitionName = "image-${user.login}-from-search"
    }

    inner class SearchUserViewHolder(val binding: UserItemBinding) : ViewHolder(binding.root)

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