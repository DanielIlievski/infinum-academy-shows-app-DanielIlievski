package com.example.infinite_movies.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.infinite_movies.R
import com.example.infinite_movies.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null

    private val binding get() = _binding!!

    private val args by navArgs<WelcomeFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.welcomeText.text = getString(R.string.welcome_text, args.username)

        //switching automatically to ShowsActivity
        val mHandler = Handler(Looper.getMainLooper())
        mHandler.postDelayed(object : Runnable {
            override fun run() {
                val directions = WelcomeFragmentDirections.toNavigation(args.username, args.email)

                findNavController().navigate(directions)
            }
        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}