package com.example.infinite_movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.infinite_movies.databinding.ItemReviewBinding
import com.example.infinite_movies.model.Review

class ReviewsAdapter(
    private var items: List<Review>
) : RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context))
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.count()

    fun addAllReviews(reviews: List<Review>) {
        items = reviews
        notifyDataSetChanged()
    }

    fun addReview(review: Review) {
        items += review
        notifyItemInserted(items.lastIndex)
    }

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Review) {
            Glide.with(binding.root.context)
                .load(item.user.imageUrl)
                .into(binding.previewProfilePhoto)
            binding.reviewUsername.text = item.user.email.substring(0, item.user.email.indexOf('@'))
            binding.starRatingText.text = item.rating.toString()

            if (item.comment.isEmpty()) {
                binding.reviewComment.text = item.comment
                binding.reviewComment.visibility = View.GONE
                binding.reviewComment.isVisible = false
            } else
                binding.reviewComment.text = item.comment
        }
    }
}