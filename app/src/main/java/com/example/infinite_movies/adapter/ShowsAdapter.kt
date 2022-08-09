package com.example.infinite_movies.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.infinite_movies.ShowCardView
import com.example.infinite_movies.model.Show

class ShowsAdapter(
    private var items: List<Show>,
    private val onItemClickCallback: (Show) -> Unit
) : RecyclerView.Adapter<ShowsAdapter.ShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val showCardView = ShowCardView(parent.context)
//        val binding = ViewShowItemBinding.inflate(LayoutInflater.from(parent.context))
        return ShowViewHolder(showCardView)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.setShow(items[position])
    }

    override fun getItemCount() = items.count()

    fun addAllItems(shows: List<Show>) {
        items = shows
        notifyDataSetChanged()
    }

    inner class ShowViewHolder(private val showCardView: ShowCardView) : RecyclerView.ViewHolder(showCardView) {

        fun setShow(item: Show) {
            showCardView.setShow(item)
            showCardView.setShowCardOnClickListener(item, onItemClickCallback)
        }
    }

}