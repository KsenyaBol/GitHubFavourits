package com.example.githubfavourits.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.objects.repository.Repositories
import com.example.githubfavourits.R

class CustomRecyclerAdapter (private var repositories: ArrayList<Repositories>):
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val repositoryNameText: TextView = itemView.findViewById(R.id.repository_name)
        val favouriteImage: ImageButton = itemView.findViewById(R.id.favourite_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_template, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.repositoryNameText.text = repositories[position].name
        holder.favouriteImage.isSelected = repositories[position].favourite
//        holder.favouriteImage.setOnClickListener {
//            repositories[position].favourite = !repositories[position].favourite
//        }

    }

    override fun getItemCount(): Int {
        return repositories.size
    }
}