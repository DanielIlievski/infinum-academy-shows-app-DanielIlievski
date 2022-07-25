package com.example.infinite_movies.fragment

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
import com.example.infinite_movies.adapter.ReviewsAdapter
import com.example.infinite_movies.databinding.DialogAddReviewBinding
import com.example.infinite_movies.databinding.FragmentShowDetailsBinding
import com.example.infinite_movies.viewModel.ShowDetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null

    private val binding get() = _binding!!

    private lateinit var adapter: ReviewsAdapter

    private val args by navArgs<ShowDetailsFragmentArgs>()

    private val viewModel by viewModels<ShowDetailsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.reviewsLiveData.observe(viewLifecycleOwner) { reviewsList ->
            adapter.addAllReviews(reviewsList)
        }

        viewModel.ratingBarRating.observe(viewLifecycleOwner) { ratingBarRating ->
            binding.ratingBar.rating = ratingBarRating
        }

        viewModel.ratingBarText.observe(viewLifecycleOwner) { ratingBarText ->
            binding.ratingBarText.text = ratingBarText
        }

        viewModel.reviewAdd.observe(viewLifecycleOwner) { review ->
            adapter.addReview(review)
        }

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

        binding.showDetailsToolbar.title = title
        binding.collapseBarImage.setImageResource(imgResId)
        binding.nestedScrollViewText.text = description

        viewModel.addRatingBarStats()
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
            viewModel.addReviewToList(
                args.username,
                bottomSheetBinding.writeReviewTextField.editText?.text.toString(),
                bottomSheetBinding.reviewRatingBar.rating.toInt()
            )
            dialog.dismiss()
        }

        dialog.show()
    }
}