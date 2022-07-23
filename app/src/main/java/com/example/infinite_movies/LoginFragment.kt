package com.example.infinite_movies

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.infinite_movies.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
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

            val directions = LoginFragmentDirections.toWelcomeFragment(username)

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
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.loginButton.isEnabled = isButtonEnabled()

                if (isPasswordLongEnough(binding.passwordTextField.editText?.text.toString()))
                    binding.passwordTextField.error = null
                else {
                    binding.passwordTextField.error = "Password must contain at least 6 characters"
                    // enables password visibility toggle button to be visible when having and error message
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
    }
}