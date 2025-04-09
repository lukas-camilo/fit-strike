package com.lucascamilo.fitstrike.presentation.ui

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.lucascamilo.fitstrike.R
import com.lucascamilo.fitstrike.databinding.ActivitySignUpBinding
import com.lucascamilo.fitstrike.domain.exception.UsernameExistsException
import com.lucascamilo.fitstrike.domain.exception.VerificationCodeRequiredException
import com.lucascamilo.fitstrike.presentation.ui.dialog.ConfirmationCodeDialogFragment
import com.lucascamilo.fitstrike.presentation.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val loadingView = layoutInflater.inflate(R.layout.loading_layout, null)
        val rootView = findViewById<ViewGroup>(android.R.id.content)

        binding.btnSignUp.setOnClickListener {
            rootView.addView(loadingView)
            signUpViewModel.register(
                email = binding.edtTextEmail.text.toString(),
                password = binding.edtTextPassword.text.toString(),
                onResult = {
                    finish()
                },
                onError = { error ->
                    rootView.removeView(loadingView)

                    if (error is VerificationCodeRequiredException) {
                        val bottomSheet = ConfirmationCodeDialogFragment { code ->
                            rootView.addView(loadingView)
                            signUpViewModel.confirmUser(
                                error.email,
                                code,
                                {
                                    finish()
                                }, { confirmError ->
                                    Toast.makeText(this, "Erro ao confirmar usuário: ${confirmError.message}", Toast.LENGTH_LONG).show()
                                }
                            )
                        }
                        bottomSheet.show(supportFragmentManager, "ConfirmationCodeDialogFragment")
                    } else if (error is UsernameExistsException) {
                        Toast.makeText(this, "Usuário já cadastrado!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Erro ao cadastrar: ${error.message}", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }

        binding.txvLogIn.setOnClickListener {
            finish()
        }

        // Observa as mensagens de validação
        signUpViewModel.validationMessages.observe(this) { messages ->
            binding.txvError.text = messages.firstOrNull() ?: ""
            binding.btnSignUp.isEnabled = signUpViewModel.isFormValid()
        }

        // Adiciona listener para o campo de e-mail
        binding.edtTextEmail.addTextChangedListener {
            val email = binding.edtTextEmail.text.toString()
            signUpViewModel.validateEmail(email)
        }

        // Adiciona listeners para os campos de senha e confirmar senha
        val textWatcher = {
            val password = binding.edtTextPassword.text.toString()
            val confirmPassword = binding.edtTextConfirmPassword.text.toString()
            signUpViewModel.validatePassword(password, confirmPassword)
        }

        binding.edtTextPassword.addTextChangedListener { textWatcher() }
        binding.edtTextConfirmPassword.addTextChangedListener { textWatcher() }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}