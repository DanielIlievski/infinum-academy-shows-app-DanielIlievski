package com.example.infinite_movies.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.infinite_movies.R
import com.example.infinite_movies.adapter.ReviewsAdapter
import com.example.infinite_movies.databinding.DialogAddReviewBinding
import com.example.infinite_movies.databinding.FragmentShowDetailsBinding
import com.example.infinite_movies.model.ReviewRequest
import com.example.infinite_movies.networking.ApiModule
import com.example.infinite_movies.viewModel.ShowDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: ReviewsAdapter

    private val args by navArgs<ShowDetailsFragmentArgs>()

    private val viewModel by viewModels<ShowDetailsViewModel>()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE)

        ApiModule.initRetrofit(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchShow(args.id)

        viewModel.fetchReviews(args.id)

        initAssignValues()

        initListeners()

        initReviewsRecycler()

        initLoadReviewsButton()

        initAddReviewButton()
    }

    private fun initAssignValues() {
        viewModel.singleShowLiveData.observe(viewLifecycleOwner) { show ->
            val title = show.title
            val imgUrl = show.imgUrl
            val description = show.description
            val avgRating = show.avgRating
            val numberOfReviews = show.numberOfReviews

            binding.showDetailsCollapsingToolbar.title = title
            Glide.with(binding.root.context)
                .load(imgUrl)
                .placeholder(R.drawable.ic_review_profile)
                .into(binding.collapseBarImage)
            binding.nestedScrollViewText.text = description
            binding.ratingBar.rating = show.avgRating!!
            binding.ratingBarText.text = getString(R.string.ratingBarText, numberOfReviews, avgRating)

        }

        viewModel.reviewsLiveData.observe(viewLifecycleOwner) { reviewsList ->
            adapter.addAllReviews(reviewsList)
        }

        viewModel.reviewAdd.observe(viewLifecycleOwner) { review ->
            adapter.addReview(review)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initListeners() {
        (activity as AppCompatActivity).setSupportActionBar(binding.showDetailsToolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initReviewsRecycler() {
        adapter = ReviewsAdapter(emptyList())

        binding.reviewsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.reviewsRecycler.adapter = adapter

        binding.reviewsRecycler.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
    }

    private fun initLoadReviewsButton() {
        binding.reviews.setOnClickListener {
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
            val reviewRequest = ReviewRequest(
                bottomSheetBinding.reviewRatingBar.rating.toInt(),
                bottomSheetBinding.writeReviewTextField.editText?.text.toString(),
                args.id
            )
            viewModel.createReview(reviewRequest)
            dialog.dismiss()
        }

        dialog.show()
    }
}