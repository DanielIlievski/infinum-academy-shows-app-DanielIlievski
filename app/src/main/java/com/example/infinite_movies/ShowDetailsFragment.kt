package com.example.infinite_movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.infinite_movies.databinding.DialogAddReviewBinding
import com.example.infinite_movies.databinding.FragmentShowDetailsBinding
import com.example.infinite_movies.model.Review
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: ReviewsAdapter

    private val args by navArgs<ShowDetailsFragmentArgs>()

    private var reviews = listOf(
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

    private fun getAvgRatingStars(): Float {
        var stars = 0
        for (review in reviews) {
            stars += review.ratingStars
        }
        return stars.toFloat() / reviews.count()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAssignValues()

        initListeners()

        initReviewsRecycler()

        initLoadReviewsButton()

        initAddReviewButton()
    }

    private fun initAssignValues() {
        val title = args.showName
        val imgResId = args.showImageResourceId
        val description = args.showDescription

        binding.toolbar.title = title
        binding.collapseBarImage.setImageResource(imgResId)
        binding.nestedScrollViewText.text = description

        binding.ratingBar.rating = getAvgRatingStars()

        binding.ratingBarText.text =
            getString(
                R.string.ratingBarText,
                reviews.count().toString(),
                String.format("%.2f", getAvgRatingStars())
            )
    }

    private fun initListeners() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener(View.OnClickListener {
            val directions = ShowDetailsFragmentDirections.toShowsFragment(args.username)

            findNavController().navigate(directions)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initReviewsRecycler() {
        adapter = ReviewsAdapter(reviews)

        binding.reviewsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.reviewsRecycler.adapter = adapter

        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
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
        val dialog = BottomSheetDialog(requireContext())

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
        val review = Review(reviews.count() + 1, args.username, comment, numStars, R.drawable.ic_review_profile)
        adapter.addReview(review)
        reviews += review
        binding.ratingBar.rating = getAvgRatingStars()

        binding.ratingBarText.text =
            getString(
                R.string.ratingBarText,
                reviews.count().toString(),
                String.format("%.2f", getAvgRatingStars())
            )
    }
}