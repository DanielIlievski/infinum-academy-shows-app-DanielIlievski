package com.example.infinite_movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.infinite_movies.R
import com.example.infinite_movies.databinding.ViewShowItemBinding
import com.example.infinite_movies.model.Show

class ShowsAdapter(
    private var items: List<Show>,
    private val onItemClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

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

    inner class ShowViewHolder(private val binding: ViewShowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show) {
            binding.showName.text = item.title
            Glide.with(binding.root.context)
                .load(item.imgUrl)
                .override(1400)
                .placeholder(R.drawable.progress_spinner_animation)
                .into(binding.showImage)
            binding.showDescription.text = item.description
            binding.cardContainer.setOnClickListener {
                onItemClickCallback(item)
            }
        }
    }

}