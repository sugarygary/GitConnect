package com.sugarygary.gitconnect.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sugarygary.gitconnect.R
import com.sugarygary.gitconnect.data.repository.model.UserModel
import com.sugarygary.gitconnect.databinding.UserItemBinding
import com.sugarygary.gitconnect.utils.glide

class SearchUserAdapter(
    private val onClick: (ImageView, String) -> Unit,
    private val onClickFavorite: (Boolean, UserModel) -> Unit
) :
    ListAdapter<UserModel, SearchUserAdapter.SearchUserViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) {
        val user = getItem(position)
        if (user.avatarUrl != null) {
            holder.binding.ivProfile.glide(user.avatarUrl)
        }
        if (user.isFavorite) {
            holder.binding.ibFavorite.setImageResource(R.drawable.favorite)
        } else {
            holder.binding.ibFavorite.setImageResource(R.drawable.not_favorite)
        }
        holder.binding.tvUsername.text = user.login
        holder.binding.root.setOnClickListener {
            onClick.invoke(holder.binding.ivProfile, user.login)
        }
        holder.binding.ibFavorite.setOnClickListener {
            onClickFavorite.invoke(user.isFavorite, user)
        }
        //beri attribute transitionName agar dikenali oleh MaterialContainerTransform
        holder.binding.ivProfile.transitionName = "image-${user.login}-from-search"
    }

    inner class SearchUserViewHolder(val binding: UserItemBinding) : ViewHolder(binding.root)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserModel>() {
            override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}