package com.example.infinite_movies.fragment

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.infinite_movies.R
import com.example.infinite_movies.databinding.FragmentLoginBinding

private const val IS_CHECKED = "IS_CHECKED"
private const val EMAIL = "EMAIL"
private const val PASSWORD = "PASSWORD"

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val FIVE = 5
    }

    private fun isValidEmail(email: String): Boolean {
        //return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordLongEnough(password: String): Boolean {
        return password.length > FIVE
    }

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        val isRememberMeChecked = sharedPreferences.getBoolean(IS_CHECKED, false)

        if (isRememberMeChecked) {
            val email = sharedPreferences.getString(EMAIL, "example.email@gmail.com").toString()
            val username = email.substring(0, email.indexOf('@'))

            val directions = LoginFragmentDirections.toWelcomeFragment(username = username, email = email)

            findNavController().navigate(directions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.loginButton.setOnClickListener {
            // extract the characters before the @
            val username = binding.emailTextField.editText?.text.toString()
                .substring(0, binding.emailTextField.editText?.text.toString().indexOf('@'))

            val email = binding.emailTextField.editText?.text.toString()

            val directions = LoginFragmentDirections.toWelcomeFragment(username, email)

            findNavController().navigate(directions)
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
                    binding.emailTextField.error = "Invalid email!"
                if (binding.loginButton.isEnabled) {
                    binding.loginButton.setBackgroundColor(Color.WHITE)
                    binding.loginButton.setTextColor(resources.getColor(R.color.purple_background))
                } else {
                    binding.loginButton.setBackgroundColor(resources.getColor(R.color.grey_disabled))
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
                    binding.passwordTextField.error = "Password must contain at least 6 characters"
                    // enables password visibility toggle button to be visible when having an error message
                    binding.passwordTextField.errorIconDrawable = null
                }
                if (binding.loginButton.isEnabled) {
                    binding.loginButton.setBackgroundColor(Color.WHITE)
                    binding.loginButton.setTextColor(resources.getColor(R.color.purple_background))
                } else {
                    binding.loginButton.setBackgroundColor(resources.getColor(R.color.grey_disabled))
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