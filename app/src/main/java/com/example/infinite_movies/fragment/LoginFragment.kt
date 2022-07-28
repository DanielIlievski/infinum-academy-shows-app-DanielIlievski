package com.example.infinite_movies.fragment

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.infinite_movies.R
import com.example.infinite_movies.SessionManager
import com.example.infinite_movies.databinding.FragmentLoginBinding
import com.example.infinite_movies.model.LoginRequest
import com.example.infinite_movies.model.LoginResponse
import com.example.infinite_movies.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val IS_CHECKED = "IS_CHECKED"
private const val EMAIL = "EMAIL"
private const val PASSWORD = "PASSWORD"
private const val FIVE = 5

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    private val args by navArgs<LoginFragmentArgs>()

    private lateinit var sessionManager: SessionManager

    private fun isValidEmail(email: String): Boolean {
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

        val isRememberMeChecked = sharedPreferences.getBoolean(IS_CHECKED, false)

        if (args.registerFlag) {
            binding.loginText.text = getString(R.string.registrationSuccess)
            binding.registerButton.isVisible = false
        }

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
        binding.registerButton.setOnClickListener {
            val directions = LoginFragmentDirections.toRegisterFragment()

            findNavController().navigate(directions)
        }

        binding.loginButton.setOnClickListener {
            // extract the characters before the @
            val username = binding.emailTextField.editText?.text.toString()
                .substring(0, binding.emailTextField.editText?.text.toString().indexOf('@'))

            val email = binding.emailTextField.editText?.text.toString()

            val password = binding.passwordTextField.editText?.text.toString()

            val loginRequest = LoginRequest(
                email = email,
                password = password
            )

            ApiModule.retrofit.login(loginRequest)
                .enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        val accessToken = response.headers()["access-token"].toString()
                        val client = response.headers()["client"].toString()
                        val uid = response.headers()["uid"].toString()

                        sessionManager.saveAuthToken(accessToken)
                        sessionManager.saveClient(client)
                        sessionManager.saveUid(uid)

                        if (response.code() == 201) {
                            val directions = LoginFragmentDirections.toWelcomeFragment(username, email)

                            findNavController().navigate(directions)
                        }
                        else if (response.code() == 401){
                            Toast.makeText(requireContext(), "Invalid login credentials. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        TODO("Not yet implemented")
                    }

                })
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