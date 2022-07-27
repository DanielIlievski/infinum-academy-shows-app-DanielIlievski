package com.example.infinite_movies.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.infinite_movies.R
import com.example.infinite_movies.databinding.FragmentRegisterBinding

private const val EMAIL = "EMAIL"
private const val PASSWORD = "PASSWORD"
private const val FIVE = 5

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordLongEnough(password: String): Boolean {
        return password.length > FIVE
    }

    private fun doPasswordsMatch(password: String, repeatPassword: String): Boolean {
        return password.equals(repeatPassword)
    }

    private fun isButtonEnabled(): Boolean {
        return isValidEmail(binding.emailTextField.editText?.text.toString()) &&
            isPasswordLongEnough(binding.passwordTextField.editText?.text.toString()) &&
            doPasswordsMatch(binding.passwordTextField.editText?.text.toString(), binding.repeatPasswordTextField.editText?.text.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

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
        binding.registerButton.setOnClickListener {
            val directions = RegisterFragmentDirections.toLoginFragment(registerFlag = true)

            findNavController().navigate(directions)
        }

        binding.emailTextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.registerButton.isEnabled = isButtonEnabled()
                // set error message for invalid email
                if (isValidEmail(binding.emailTextField.editText?.text.toString()))
                    binding.emailTextField.error = null
                else
                    binding.emailTextField.error = "Invalid email!"
                if (binding.registerButton.isEnabled) {
                    binding.registerButton.setBackgroundColor(Color.WHITE)
                    binding.registerButton.setTextColor(resources.getColor(R.color.purple_background))
                } else {
                    binding.registerButton.setBackgroundColor(resources.getColor(R.color.grey_disabled))
                    binding.registerButton.setTextColor(Color.WHITE)
                }
            }
        })

        binding.passwordTextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.registerButton.isEnabled = isButtonEnabled()

                if (isPasswordLongEnough(binding.passwordTextField.editText?.text.toString()))
                    binding.passwordTextField.error = null
                else {
                    binding.passwordTextField.error = "Password must contain at least 6 characters"
                    // enables password visibility toggle button to be visible when having an error message
                    binding.passwordTextField.errorIconDrawable = null
                }
                if (binding.registerButton.isEnabled) {
                    binding.registerButton.setBackgroundColor(Color.WHITE)
                    binding.registerButton.setTextColor(resources.getColor(R.color.purple_background))
                } else {
                    binding.registerButton.setBackgroundColor(resources.getColor(R.color.grey_disabled))
                    binding.registerButton.setTextColor(Color.WHITE)
                }
            }
        })

        binding.repeatPasswordTextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                binding.registerButton.isEnabled = isButtonEnabled()

                if (doPasswordsMatch(
                        binding.passwordTextField.editText?.text.toString(),
                        binding.repeatPasswordTextField.editText?.text.toString()
                    )
                ) {
                    binding.repeatPasswordTextField.error = null
                    binding.passwordTextField.error = null
                } else {
                    binding.passwordTextField.error = "Passwords do not match!"
                    binding.repeatPasswordTextField.error = "Passwords do not match!"
                    // enables password visibility toggle button to be visible when having an error message
                    binding.repeatPasswordTextField.errorIconDrawable = null
                }
                if (binding.registerButton.isEnabled) {
                    binding.registerButton.setBackgroundColor(Color.WHITE)
                    binding.registerButton.setTextColor(resources.getColor(R.color.purple_background))
                } else {
                    binding.registerButton.setBackgroundColor(resources.getColor(R.color.grey_disabled))
                    binding.registerButton.setTextColor(Color.WHITE)
                }
            }
        })
    }
}