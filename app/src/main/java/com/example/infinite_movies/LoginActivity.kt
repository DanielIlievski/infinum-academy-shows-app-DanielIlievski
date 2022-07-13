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

    lateinit var binding: ActivityLoginBinding

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
        return password.length > 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // disable and enable the Login button when email and password conditions are fulfilled
        binding.emailTextField.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                binding.loginButton.isEnabled =
                    isValidEmail(binding.emailTextField.editText?.text.toString()) && isPasswordLongEnough(
                        binding.passwordTextField.editText?.text.toString()
                    )
                // set error message for invalid email
                if (isValidEmail(binding.emailTextField.editText?.text.toString()))
                    binding.emailTextField.error = null
                else
                    binding.emailTextField.error = "Invalid email!"

                if (binding.loginButton.isEnabled) {
                    binding.loginButton.setBackgroundColor(Color.WHITE)
                    binding.loginButton.setTextColor(Color.parseColor("#3D1D72"))
                } else {
                    binding.loginButton.setBackgroundColor(Color.parseColor("#BBBBBB"))
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
                binding.loginButton.isEnabled =
                    isPasswordLongEnough(binding.passwordTextField.editText?.text.toString()) && isValidEmail(
                        binding.emailTextField.editText?.text.toString()
                    )

                if (isPasswordLongEnough(binding.passwordTextField.editText?.text.toString()))
                    binding.passwordTextField.error = null
                else {
                    binding.passwordTextField.error = "Password must contain at least 6 characters"
                    // enables password visibility toggle button to be visible when having and error message
                    binding.passwordTextField.errorIconDrawable = null
                }

                if (binding.loginButton.isEnabled) {
                    binding.loginButton.setBackgroundColor(Color.WHITE)
                    binding.loginButton.setTextColor(Color.parseColor("#3D1D72"))
                } else {
                    binding.loginButton.setBackgroundColor(Color.parseColor("#BBBBBB"))
                    binding.loginButton.setTextColor(Color.WHITE)
                }
            }
        })

        binding.loginButton.setOnClickListener {

            val intent = Intent(this, WelcomeActivity::class.java)

            // extract the characters before the @
            val username = binding.emailTextField.editText?.text.toString()
                .substring(0, binding.emailTextField.editText?.text.toString().indexOf('@'))

            intent.putExtra("EXTRA_EMAIL", username)
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