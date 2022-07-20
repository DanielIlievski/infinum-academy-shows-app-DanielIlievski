package com.example.infinite_movies

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.infinite_movies.databinding.ActivityLoginBinding
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private const val EXTRA_MAIL = "EXTRA_MAIL"
        private const val FIVE = 5
    }

    //    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
    //        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
    //                "\\@" +
    //                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
    //                "(" +
    //                "\\." +
    //                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
    //                ")+"
    //    )

    private fun isValidEmail(email: String): Boolean {
        //return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isPasswordLongEnough(password: String): Boolean {
        return password.length > FIVE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        fun isButtonEnabled(): Boolean {
            return isValidEmail(binding.emailTextField.editText?.text.toString()) && isPasswordLongEnough(
                binding.passwordTextField.editText?.text.toString()
            )
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
                    binding.loginButton.setTextColor(getColor(R.color.purple_background))
                } else {
                    binding.loginButton.setBackgroundColor(getColor(R.color.grey_disabled))
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
                    binding.loginButton.setTextColor(getColor(R.color.purple_background))
                } else {
                    binding.loginButton.setBackgroundColor(getColor(R.color.grey_disabled))
                    binding.loginButton.setTextColor(Color.WHITE)
                }
            }
        })

        binding.loginButton.setOnClickListener {

            val intent = Intent(this, WelcomeActivity::class.java)

            // extract the characters before the @
            val username = binding.emailTextField.editText?.text.toString()
                .substring(0, binding.emailTextField.editText?.text.toString().indexOf('@'))

            intent.putExtra(EXTRA_MAIL, username)
            startActivity(intent)

            /* Starting WelcomeActivity.kt with an implicit intent */
            //            val intent = Intent(Intent.ACTION_VIEW)
            //            // extract the characters before the @
            //            val username = binding.emailTextField.editText?.text.toString()
            //                .substring(0, binding.emailTextField.editText?.text.toString().indexOf('@'))
            //            intent.putExtra("EXTRA_EMAIL", username)
            //            intent.type = "text/plain"
            //            startActivity(intent)
        }

    }
}