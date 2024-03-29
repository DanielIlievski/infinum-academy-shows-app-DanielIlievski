package com.example.infinite_movies.fragment

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.infinite_movies.EMAIL
import com.example.infinite_movies.IS_CHECKED
import com.example.infinite_movies.PASSWORD
import com.example.infinite_movies.R
import com.example.infinite_movies.SessionManager
import com.example.infinite_movies.databinding.FragmentLoginBinding
import com.example.infinite_movies.errorAlertDialog
import com.example.infinite_movies.isPasswordLongEnough
import com.example.infinite_movies.isValidEmail
import com.example.infinite_movies.networking.ApiModule
import com.example.infinite_movies.viewModel.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val args by navArgs<LoginFragmentArgs>()

    private lateinit var sessionManager: SessionManager

    private val viewModel by viewModels<LoginViewModel>()

    private fun isButtonEnabled(): Boolean {
        return isValidEmail(binding.emailTextField.editText?.text.toString()) && isPasswordLongEnough(
            binding.passwordTextField.editText?.text.toString()
        )
    }

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

        ApiModule.initRetrofit(requireContext())

        sessionManager = SessionManager(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        initAnimations()

        val isRememberMeChecked = sharedPreferences.getBoolean(IS_CHECKED, false)

        if (args.registerFlag) {
            binding.loginText.text = getString(R.string.registration_success)
            binding.registerButton.isVisible = false
        }

        if (isRememberMeChecked) {
            val email = sharedPreferences.getString(EMAIL, "example.email@gmail.com").toString()
            val username = email.substring(0, email.indexOf('@'))

            val directions = LoginFragmentDirections.toWelcomeFragment(username = username, email = email)

            findNavController().navigate(directions)
        }
    }

    private fun initAnimations() {
        animateTriangleImageViewAndShowsTextView()
    }

    private fun animateTriangleImageViewAndShowsTextView(): ViewPropertyAnimator? = with(binding) {
        triangle.translationY = -800f
        showsText.alpha = 0f
        showsText.scaleX = 0f
        showsText.scaleY = 0f
        triangle.animate().apply {
            duration = 1000
            translationY(0f)
            interpolator = BounceInterpolator()
        }.withEndAction {
            showsText.animate().apply {
                duration = 800
                alpha(1f)
                scaleX(1f)
                scaleY(1f)
                interpolator = OvershootInterpolator(4f)
            }.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.registerButton.setOnClickListener {
            val directions = LoginFragmentDirections.toRegisterFragment()

            findNavController().navigate(directions)
        }

        binding.loginButton.setOnClickListener {

            val email = binding.emailTextField.editText?.text.toString()
            // extract the characters before the @
            val username = email
                .substring(0, email.indexOf('@'))

            val password = binding.passwordTextField.editText?.text.toString()

            viewModel.accessTokenLiveData.observe(viewLifecycleOwner) { accessToken ->
                sessionManager.saveAuthToken(accessToken)
            }

            viewModel.clientLiveData.observe(viewLifecycleOwner) { client ->
                sessionManager.saveClient(client)
            }

            viewModel.uidLiveData.observe(viewLifecycleOwner) { uid ->
                sessionManager.saveUid(uid)
            }

            viewModel.responseCodeLiveData.observe(viewLifecycleOwner) { responseCode ->
                when (responseCode) {
                    201 -> {
                        val directions = LoginFragmentDirections.toWelcomeFragment(username, email)

                        findNavController().navigate(directions)
                    }
                    401 -> {
                        errorAlertDialog(requireContext(), getString(R.string.invalid_login_credentials))
                    }
                }
            }

            viewModel.login(email, password)
        }

        // disable and enable the Login button when email and password conditions are fulfilled
        binding.emailTextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.loginButton.isEnabled = isButtonEnabled()
                // set error message for invalid email
                if (isValidEmail(binding.emailTextField.editText?.text.toString()))
                    binding.emailTextField.error = null
                else
                    binding.emailTextField.error = getString(R.string.invalid_email)
                if (binding.loginButton.isEnabled) {
                    binding.loginButton.setBackgroundColor(Color.WHITE)
                    binding.loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_background))
                } else {
                    binding.loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_disabled))
                    binding.loginButton.setTextColor(Color.WHITE)
                }
            }
        })

        binding.passwordTextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.loginButton.isEnabled = isButtonEnabled()

                if (isPasswordLongEnough(binding.passwordTextField.editText?.text.toString()))
                    binding.passwordTextField.error = null
                else {
                    binding.passwordTextField.error = getString(R.string.password_condition)
                    // enables password visibility toggle button to be visible when having an error message
                    binding.passwordTextField.errorIconDrawable = null
                }
                if (binding.loginButton.isEnabled) {
                    binding.loginButton.setBackgroundColor(Color.WHITE)
                    binding.loginButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_background))
                } else {
                    binding.loginButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey_disabled))
                    binding.loginButton.setTextColor(Color.WHITE)
                }
            }
        })

        binding.rememberMeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            val email = binding.emailTextField.editText?.text.toString()
            val password = binding.passwordTextField.editText?.text.toString()

            sharedPreferences.edit {
                putBoolean(IS_CHECKED, isChecked)
            }

            if (isChecked) {
                sharedPreferences.edit {
                    putString(EMAIL, email)
                    putString(PASSWORD, password)
                }
            }
        }
    }
}