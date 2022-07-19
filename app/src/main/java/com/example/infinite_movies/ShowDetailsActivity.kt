package com.example.infinite_movies

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinite_movies.databinding.ActivityShowDetailsBinding
import com.example.infinite_movies.databinding.DialogAddReviewBinding
import com.example.infinite_movies.model.Review
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        private const val EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION"
        private const val EXTRA_IMAGE = "EXTRA_IMAGE"
        private const val EXTRA_USERNAME = "EXTRA_USERNAME"
        fun getExtraTitle(): String {
            return EXTRA_TITLE
        }

        fun getExtraDescription(): String {
            return EXTRA_DESCRIPTION
        }

        fun getExtraImage(): String {
            return EXTRA_IMAGE
        }

        fun buildIntent(
            activity: Activity,
            title: String,
            description: String,
            image: Int,
            username: String
        ): Intent {
            val intent = Intent(activity, ShowDetailsActivity::class.java)
            intent.putExtra(EXTRA_TITLE, title)
            intent.putExtra(EXTRA_DESCRIPTION, description)
            intent.putExtra(EXTRA_IMAGE, image)
            intent.putExtra(EXTRA_USERNAME, username)
            return intent
        }
    }

    private val reviews = listOf(
        Review(1, "daniel.ilievski", "Great show!", 5, R.drawable.ic_review_profile),
        Review(2, "petar.petrovski", "", 2, R.drawable.ic_review_profile),
        Review(3, "marko.markoski", "I laughed so much!", 4, R.drawable.ic_review_profile),
        Review(4, "ivan.ivanovski", "Relaxing show.", 4, R.drawable.ic_review_profile),
        Review(5, "andrej.krsteski", "It was ok.", 3, R.drawable.ic_review_profile),
        Review(6, "janko.stojanovski", "", 1, R.drawable.ic_review_profile),
        Review(7, "martin.stojceski", "", 5, R.drawable.ic_review_profile),
        Review(8, "viktor.smilevski", "Loved it!", 4, R.drawable.ic_review_profile),
        Review(9, "stefan.dimeski", "I like it.", 3, R.drawable.ic_review_profile),
        Review(10, "petko.petkoski", "", 3, R.drawable.ic_review_profile)
    )

    private lateinit var binding: ActivityShowDetailsBinding

    private lateinit var adapter: ReviewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.extras?.getString(getExtraTitle())
        binding.toolbar.title = title

        //        binding.collapsingAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        //            val actionBar = supportActionBar
        //            val toolbarCollapsed = Math.abs(verticalOffset) >= appBarLayout.totalScrollRange
        //            if(toolbarCollapsed)
        //                binding.toolbar.title = title
        //            else
        //                binding.toolbar.title = " "
        //        })

        //        var isShow = true
        //        var scrollRange = -1
        //        binding.collapsingAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
        //            if (scrollRange == -1){
        //                scrollRange = barLayout?.totalScrollRange!!
        //            }
        //            if (scrollRange + verticalOffset == 0){
        //                binding.toolbar.title = title
        //                isShow = true
        //            } else if (isShow){
        //                binding.toolbar.title = " " //careful there should a space between double quote otherwise it wont work
        //                isShow = false
        //            }
        //        })
        val imgResId = intent.extras!!.getInt(getExtraImage())
        binding.collapseBarImage.setImageResource(imgResId)
        setSupportActionBar(binding.toolbar)

        binding.nestedScrollViewText.text = intent.extras?.getString(getExtraDescription())

        var stars = 0
        for (review in reviews) {
            stars += review.ratingStars
        }
        val avgStars = stars.toFloat() / reviews.count()
        binding.ratingBar.rating = avgStars

        binding.ratingBarText.text =
            getString(
                R.string.ratingBarText,
                reviews.count().toString(),
                String.format("%.2f", avgStars)
            )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ShowsActivity::class.java))
            finish()
        })

        initReviewsRecycler()

        initLoadReviewsButton()

        initAddReviewButton()
    }

    private fun initReviewsRecycler() {
        adapter = ReviewsAdapter(reviews)

        binding.reviewsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.reviewsRecycler.adapter = adapter

        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    private fun initLoadReviewsButton() {
        binding.reviews.setOnClickListener {
            adapter.addAllReviews(reviews)
            if (!binding.reviewsRecycler.isVisible) {
                binding.reviewsRecycler.isVisible = true
                binding.reviewEmptyStateText.isVisible = false
                binding.ratingBar.isVisible = true
                binding.ratingBarText.isVisible = true
            } else {
                binding.reviewsRecycler.isVisible = false
                binding.reviewEmptyStateText.isVisible = true
                binding.ratingBar.isVisible = false
                binding.ratingBarText.isVisible = false
            }
        }
    }

    private fun initAddReviewButton() {
        binding.writeReviewButton.setOnClickListener {
            showAddCommentBottomSheet()
        }
    }

    private fun showAddCommentBottomSheet() {
        val dialog = BottomSheetDialog(this)

        val bottomSheetBinding = DialogAddReviewBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.submitButton.setOnClickListener {
            addReviewToList(
                bottomSheetBinding.writeReviewTextField.editText?.text.toString(),
                bottomSheetBinding.reviewRatingBar.rating.toInt()
            )
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun addReviewToList(comment: String, numStars: Int) {
        adapter.addReview(Review(reviews.count() + 1, "ivan.toshev", comment, numStars, R.drawable.ic_review_profile))
    }
}