package com.example.myapplication.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.UserItemBinding
import com.example.myapplication.response.ItemsItem
import com.example.myapplication.ui.DetailActivity


class UserAdapter(val listUser: List<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val dataUser = listUser[position]
        Glide.with(holder.itemView.context)
            .load(dataUser.avatarUrl)
            .into(holder.binding.itemPhoto)
        holder.binding.itemUsername.text = dataUser.login
        holder.binding.itemName.text = dataUser.name

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.USERNAME, dataUser.login)
            intentDetail.putExtra(DetailActivity.AVATAR, dataUser.avatarUrl)
            intentDetail.putExtra(DetailActivity.NAME, dataUser.name)
            holder.itemView.context.startActivity(intentDetail)
        }
    }
}