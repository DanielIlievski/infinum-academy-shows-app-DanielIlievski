package com.example.infinite_movies

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.example.infinite_movies.databinding.ViewShowItemBinding
import com.example.infinite_movies.model.Show

class ShowCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr) {

    lateinit var binding: ViewShowItemBinding

    init {
        binding = ViewShowItemBinding.inflate(LayoutInflater.from(context), this)

        clipToPadding = false

        setPadding(
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x),
            context.resources.getDimensionPixelSize(R.dimen.spacing_1x)
        )
    }

    fun setShow(item: Show) = with(binding) {
        setShowCardImage(item.imgUrl)
        item.description?.let { setShowCardDescription(it) }
        setShowCardTitle(item.title)
    }

    private fun setShowCardImage(imgUrl: String) = with(binding){
        Glide.with(binding.root.context)
            .load(imgUrl)
            .override(1400)
            .placeholder(R.drawable.ic_progress_spinner)
            .into(binding.showImage)
    }

    private fun setShowCardDescription(showDescription: String) = with(binding){
        binding.showDescription.text = showDescription
    }

    private fun setShowCardTitle(title: String) = with(binding){
        binding.showName.text = title
    }

    fun setShowCardOnClickListener(item: Show, onItemClickCallback: (Show) -> Unit) = with(binding){
        binding.cardContainer.setOnClickListener {
            onItemClickCallback(item)
        }
    }
}