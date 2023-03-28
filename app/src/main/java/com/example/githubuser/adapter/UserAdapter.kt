package com.example.githubuser.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.DisplayUser
import com.example.githubuser.data.entity.FavoriteUser
import com.example.githubuser.databinding.ItemUserBinding

class UserAdapter(private val userList: ArrayList<DisplayUser>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }


    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val users = userList[position]

        holder.binding.apply {
            tvUsersName.text = users.login
            Glide.with(holder.itemView.context)
                .load(users.avatarUrl)
                .into(userPhoto)
        }
        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(users) }


    }
    override fun getItemCount(): Int = userList.size

    interface OnItemClickCallback{
        fun onItemClicked(user: DisplayUser)
    }


}