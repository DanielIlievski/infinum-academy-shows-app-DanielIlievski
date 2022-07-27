package com.example.infinite_movies.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toIcon
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.infinite_movies.BuildConfig
import com.example.infinite_movies.R
import com.example.infinite_movies.adapter.ShowsAdapter
import com.example.infinite_movies.databinding.DialogChangeProfilePhotoBinding
import com.example.infinite_movies.databinding.DialogProfileSettingsBinding
import com.example.infinite_movies.databinding.FragmentShowsBinding
import com.example.infinite_movies.viewModel.ShowsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File

private const val PHOTO_PICKER_REQUEST_CODE = 200

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private val args by navArgs<ShowsFragmentArgs>()

    private lateinit var adapter: ShowsAdapter

    private lateinit var sharedPreferences: SharedPreferences

    private val viewModel by viewModels<ShowsViewModel>()

    private lateinit var profileSettingsBinding: DialogProfileSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("Login", Context.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileSettingsBinding = DialogProfileSettingsBinding.inflate(layoutInflater)

        binding.profileSettingsButton.setImageResource(R.drawable.ic_review_profile)

        val previewProfilePhoto = sharedPreferences.getString("PROFILE_PHOTO", "")?.toUri()
        if (previewProfilePhoto.toString() != "")
            binding.profileSettingsButton.setImageURI(previewProfilePhoto)

        viewModel.showsLiveData.observe(viewLifecycleOwner) { showList ->
            adapter.addAllItems(showList)
        }

        initListeners()

        initShowsRecycler()

        initLoadShowsButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initListeners() {
        binding.profileSettingsButton.setOnClickListener {
            showProfileSettingsBottomSheet()
        }
    }

    private fun showProfileSettingsBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())

        profileSettingsBinding = DialogProfileSettingsBinding.inflate(layoutInflater)
        dialog.setContentView(profileSettingsBinding.root)

        profileSettingsBinding.previewProfilePhoto.setImageURI(sharedPreferences.getString("PROFILE_PHOTO", "")?.toUri())
        profileSettingsBinding.profileEmail.text = args.email

        profileSettingsBinding.removeProfilePhoto.setOnClickListener {
            sharedPreferences.edit().remove("PROFILE_PHOTO").apply()
            dialog.dismiss()
        }

        profileSettingsBinding.logoutButton.setOnClickListener {
            showLogOutAlertDialog()
            dialog.dismiss()
        }

        profileSettingsBinding.changeProfilePhotoButton.setOnClickListener {
            takeOrSelectImageBottomSheet()
            dialog.dismiss()
        }

        dialog.show()
    }

    private var latestTmpUri: Uri? = null

    private fun takeOrSelectImageBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())

        val bottomSheetBinding = DialogChangeProfilePhotoBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.cameraLayout.setOnClickListener {
            takeImage()
            dialog.dismiss()
        }

        bottomSheetBinding.galleryLayout.setOnClickListener {
            //selectImage()
            Toast.makeText(requireContext(), "Not yet implemented!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                binding.profileSettingsButton.setImageURI(uri)
                sharedPreferences.edit().putString("PROFILE_PHOTO", uri.toString()).apply()
            }
        }
    }

    private fun takeImage(){
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", requireActivity().cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

    private fun showLogOutAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())

        builder.apply {
            setTitle(R.string.alertDialogTitle)
            setMessage(R.string.alertDialogMessage)
            setIcon(android.R.drawable.ic_dialog_alert)
            setPositiveButton(R.string.Yes) { dialogInterface, which ->
                sharedPreferences.edit().remove(getString(R.string.EMAIL)).apply()
                sharedPreferences.edit().remove(getString(R.string.PASSWORD)).apply()
                sharedPreferences.edit().putBoolean(getString(R.string.IS_CHECKED), false).apply()
                val directions = ShowsFragmentDirections.toLoginFragment()

                findNavController().navigate(directions)
            }
            setNegativeButton(R.string.No) { dialogInterface, which ->
                dialogInterface.dismiss()
            }
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun initShowsRecycler() {
        adapter = ShowsAdapter(emptyList()) { show ->
            /* Toast is to display text (show.name) when clicked */
            //Toast.makeText(requireContext(), show.name, Toast.LENGTH_SHORT).show()

            val directions = ShowsFragmentDirections.toShowDetailsFragment(show.name, show.description, show.imageResourceId, args.username)

            findNavController().navigate(directions)
        }

        binding.showsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.showsRecycler.adapter = adapter
    }

    private fun initLoadShowsButton() {
        binding.showEmptyState.setOnClickListener {
            if (binding.showsRecycler.isVisible) {
                binding.showEmptyState.setText(R.string.load)
                binding.showsRecycler.isVisible = false
                binding.emptyStateLayout.isVisible = true
            } else {
                binding.showEmptyState.setText(R.string.hide)
                binding.showsRecycler.isVisible = true
                binding.emptyStateLayout.isVisible = false
            }
        }
    }
}