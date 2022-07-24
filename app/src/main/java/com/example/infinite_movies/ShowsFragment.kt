package com.example.infinite_movies

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.infinite_movies.databinding.DialogProfileSettingsBinding
import com.example.infinite_movies.databinding.FragmentShowsBinding
import com.example.infinite_movies.model.Show
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!

    private val args by navArgs<ShowsFragmentArgs>()

    private val shows = listOf(
        Show(
            1,
            "Stranger Things",
            "When a young boy vanishes, a small town uncovers a mystery involving secret experiments, terrifying supernatural forces and one strange little girl.",
            R.drawable.stranger_things_show
        ),
        Show(
            2,
            "Better Call Saul",
            "Ex-con artist Jimmy McGill turns into a small-time attorney and goes through a series of trials and tragedies, as he transforms into his alter ego Saul Goodman, a morally challenged criminal lawyer.",
            R.drawable.better_call_saul_show
        ),
        Show(
            3,
            "Friends",
            "Follow the lives of six reckless adults living in Manhattan, as they indulge in adventures which make their lives both troublesome and happening.",
            R.drawable.friends_show
        ),
        Show(
            4,
            "Game of Thrones",
            "Nine noble families wage war against each other in order to gain control over the mythical land of Westeros. Meanwhile, a force is rising after millenniums and threatens the existence of living men.",
            R.drawable.game_of_thrones_show
        ),
        Show(
            5,
            "Grey's Anatomy",
            "Surgical interns and their supervisors embark on a medical journey where they become part of heart-wrenching stories and make life-changing decisions in order to become the finest doctors.",
            R.drawable.greys_anatomy_show
        ),
        Show(
            6,
            "Peaky Blinders",
            "A notorious gang in 1919 Birmingham, England, is led by the fierce Tommy Shelby, a crime boss set on moving up in the world no matter the cost.",
            R.drawable.peaky_blinders_show
        ),
        Show(
            7,
            "The Big Bang Theory",
            "The lives of four socially awkward friends, Leonard, Sheldon, Howard and Raj, take a wild turn when they meet the beautiful and free-spirited Penny.",
            R.drawable.the_big_bang_theory_show
        ),
        Show(
            8,
            "The Flash",
            "Barry Allen, a forensic investigator in Central City, gains the power of superhuman speed from a freak accident. He decides to use it to fight crime as the Flash, a costumed superhero.",
            R.drawable.the_flash_show
        ),
        Show(
            9,
            "The Office",
            "A motley group of office workers go through hilarious misadventures at the Scranton, Pennsylvania, branch of the Dunder Mifflin Paper Company.",
            R.drawable.the_office_show
        ),
        Show(
            10,
            "The Simpsons",
            "Working-class father Homer Simpson and his dysfunctional family deal with comical situations and the ups-and-downs of life in the town of Springfield.",
            R.drawable.the_simpsons_show
        ),
        Show(
            11,
            "The Walking Dead",
            "In the wake of a zombie apocalypse, various survivors struggle to stay alive. As they search for safety and evade the undead, they are forced to grapple with rival groups and difficult choices.",
            R.drawable.the_walking_dead_show
        ),
        Show(
            11,
            "Breaking Bad",
            "A high school chemistry teacher dying of cancer teams with a former student to secure his family's future by manufacturing and selling crystal meth.",
            R.drawable.breaking_bad_show
        ),
    )

    private lateinit var adapter: ShowsAdapter

    private lateinit var sharedPreferences: SharedPreferences

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
            setPositiveButton(R.string.Yes){dialogInterface, which ->
                sharedPreferences.edit().remove("EMAIL").apply()
                sharedPreferences.edit().remove("PASSWORD").apply()
                sharedPreferences.edit().putBoolean("IS_CHECKED", false).apply()
                val directions = ShowsFragmentDirections.toLoginFragment()

                findNavController().navigate(directions)
            }
            setNegativeButton(R.string.No){dialogInterface, which ->
                dialogInterface.dismiss()
            }
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun initShowsRecycler() {
        adapter = ShowsAdapter(shows) { show ->
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
            //adapter.addAllItems(shows)
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