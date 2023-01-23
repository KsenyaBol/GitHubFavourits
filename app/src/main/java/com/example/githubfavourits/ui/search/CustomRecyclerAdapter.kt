package com.example.githubfavourits.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.data.entities.api.ResponceReposytories
import com.example.data.entities.api.ResponseUser
import com.example.githubfavourits.R


class CustomRecyclerAdapter (private var userInfo: ArrayList<ResponseUser>, private var reposInfo: List<ResponceReposytories>):
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val repositoryNameText: TextView = itemView.findViewById(R.id.repository_name)
        val favouriteImage: ImageButton = itemView.findViewById(R.id.favourite_image)
        val reposButton: ImageButton = itemView.findViewById(R.id.container_for_rep)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_template, parent, false)
        return MyViewHolder(itemView)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.reposButton.setOnClickListener {

            holder.reposButton.isSelected = !holder.reposButton.isSelected

        }

        holder.favouriteImage.setOnClickListener {

            holder.favouriteImage.isSelected = !holder.favouriteImage.isSelected
        }

        holder.repositoryNameText.text = userInfo[position].name

    }

    override fun getItemCount(): Int {
        return userInfo.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setRepositories(userInfo: ArrayList<ResponseUser>) {
        this.userInfo.clear()
        this.userInfo.addAll(userInfo)
        notifyDataSetChanged()
    }


    }


