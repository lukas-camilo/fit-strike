package com.lucascamilo.fitstrike.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.lucascamilo.fitstrike.R
import com.lucascamilo.fitstrike.domain.exception.ResendVerificationCodeRequiredException
import com.lucascamilo.fitstrike.presentation.ui.dialog.ConfirmationCodeDialogFragment
import com.lucascamilo.fitstrike.presentation.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loadingView = layoutInflater.inflate(R.layout.loading_layout, null)
        val rootView = findViewById<ViewGroup>(android.R.id.content)

        val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)
        val signUpButton = findViewById<Button>(R.id.btnSignUp)
        val forgotPasswordButton = findViewById<Button>(R.id.btnForgotPassword)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()) {
                rootView.addView(loadingView)
                authViewModel.login(
                    username = email,
                    password = password,
                    onResult = { user ->
                        rootView.removeView(loadingView)
                        authViewModel.saveUser(user)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    },
                    onError = { error ->
                        rootView.removeView(loadingView)

                        if (error is ResendVerificationCodeRequiredException) {
                            val bottomSheet = ConfirmationCodeDialogFragment { code ->
                                rootView.addView(loadingView)
                                authViewModel.confirmUser(
                                    email,
                                    code,
                                    {
                                        rootView.removeView(loadingView)
                                        Toast.makeText(this, "Tente novamente", Toast.LENGTH_SHORT).show()
                                    }, { confirmError ->
                                        rootView.removeView(loadingView)
                                        Toast.makeText(this, "Erro ao confirmar usuário: ${confirmError.message}", Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                            bottomSheet.show(supportFragmentManager, "ConfirmationCodeDialogFragment")
                        } else {
                            Toast.makeText(this, "Login Failed: ${error.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                )
            } else {
                Toast.makeText(this, "Por favor, preencha os campos de email e senha!", Toast.LENGTH_SHORT).show()
            }
        }

        signUpButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        forgotPasswordButton.setOnClickListener {
            Toast.makeText(this, "Em construção!", Toast.LENGTH_SHORT).show()
        }
    }
}