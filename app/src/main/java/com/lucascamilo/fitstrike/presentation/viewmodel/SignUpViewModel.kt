package com.lucascamilo.fitstrike.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascamilo.fitstrike.domain.exception.VerificationCodeRequiredException
import com.lucascamilo.fitstrike.domain.model.RegisterRequest
import com.lucascamilo.fitstrike.domain.model.Token
import com.lucascamilo.fitstrike.domain.usecase.ConfirmationCodeUseCase
import com.lucascamilo.fitstrike.domain.usecase.RegisterUseCase
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val registerUseCase: RegisterUseCase,
    private val confirmationCodeUseCase: ConfirmationCodeUseCase
) : ViewModel() {

    private val _validationMessages = MutableLiveData<List<String>>()
    val validationMessages: LiveData<List<String>> get() = _validationMessages

    private var isEmailValid = false
    private var isPasswordValid = false

    fun validateEmail(email: String) {
        val messages = mutableListOf<String>()
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        if (!email.matches(emailRegex.toRegex())) {
            messages.add("E-mail inválido.")
        } else {
            isEmailValid = true
        }
        updateValidationMessages(messages)
    }

    fun validatePassword(password: String, confirmPassword: String) {
        val messages = mutableListOf<String>()

        if (password.length < 8) {
            messages.add("A senha deve ter pelo menos 8 caracteres.")
        }
        if (!password.any { it.isDigit() }) {
            messages.add("A senha deve conter pelo menos 1 número.")
        }
        if (!password.any { it.isUpperCase() }) {
            messages.add("A senha deve conter pelo menos 1 letra maiúscula.")
        }
        if (!password.any { it.isLowerCase() }) {
            messages.add("A senha deve conter pelo menos 1 letra minúscula.")
        }
        if (!password.any { "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~".contains(it) }) {
            messages.add("A senha deve conter pelo menos 1 caractere especial.")
        }
        if (password != confirmPassword) {
            messages.add("As senhas não coincidem.")
        }

        isPasswordValid = messages.isEmpty()
        updateValidationMessages(messages)
    }

    private fun updateValidationMessages(messages: List<String>) {
        _validationMessages.value = messages
    }

    fun isFormValid(): Boolean {
        return isEmailValid && isPasswordValid
    }

    fun register(
        email: String,
        password: String,
        name: String,
        onResult: (Any) -> Unit,
        onError: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = registerUseCase(RegisterRequest(email, password, name))
                onResult(result)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun confirmUser(
        email: String,
        confirmationCode: String,
        onResult: (Any) -> Unit,
        onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val result = confirmationCodeUseCase(email, confirmationCode)
                onResult(result)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}