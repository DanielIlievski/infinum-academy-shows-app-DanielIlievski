package com.example.infinite_movies.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.infinite_movies.R
import com.example.infinite_movies.adapter.ShowsAdapter
import com.example.infinite_movies.databinding.DialogProfileSettingsBinding
import com.example.infinite_movies.databinding.FragmentShowsBinding
import com.example.infinite_movies.viewModel.ShowsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private val args by navArgs<ShowsFragmentArgs>()

    private lateinit var adapter: ShowsAdapter

    private lateinit var sharedPreferences: SharedPreferences

    private val viewModel by viewModels<ShowsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
        val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
        sharedPreferences = EncryptedSharedPreferences.create(
            getString(R.string.login),
            masterKeyAlias,
            requireContext(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val bottomSheetBinding = DialogProfileSettingsBinding.inflate(layoutInflater)
        dialog.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.profileEmail.text = args.email

        bottomSheetBinding.logoutButton.setOnClickListener {
            showLogOutAlertDialog()
            dialog.dismiss()
        }
        dialog.show()
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