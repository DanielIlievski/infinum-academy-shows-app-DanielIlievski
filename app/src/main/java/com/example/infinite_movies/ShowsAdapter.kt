package com.example.infinite_movies

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.infinite_movies.databinding.ViewShowItemBinding
import com.example.infinite_movies.model.Show

class ShowsAdapter (
    private var items: List<Show>,
    private val onItemClickCallback: (Show) -> Unit
): RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context))
        return ShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    fun addAllItems(shows: List<Show>) {
        items = shows
        notifyDataSetChanged()
    }

    inner class ShowViewHolder(private val binding: ViewShowItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show){
            binding.showName.text = item.name
            binding.showImage.setImageResource(item.imageResourceId)
            binding.showDescription.text = item.description
            binding.cardContainer.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }

}