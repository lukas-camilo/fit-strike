package com.lucascamilo.fitstrike.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucascamilo.fitstrike.domain.model.LoginRequest
import com.lucascamilo.fitstrike.domain.model.Token
import kotlinx.coroutines.launch
import com.lucascamilo.fitstrike.domain.model.User
import com.lucascamilo.fitstrike.domain.usecase.ConfirmationCodeUseCase
import com.lucascamilo.fitstrike.domain.usecase.GetAccessTokenUseCase
import com.lucascamilo.fitstrike.domain.usecase.LoginUseCase
import com.lucascamilo.fitstrike.domain.usecase.SaveUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val saveTokenUseCase: SaveUserUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val confirmationCodeUseCase: ConfirmationCodeUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    fun login(username: String, password: String, onResult: (User) -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                val user = loginUseCase(LoginRequest(username, password))
                withContext(Dispatchers.Main) {
                    onResult(user)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            saveTokenUseCase(user)
        }
    }

    fun getAccessToken(onResult: (String?) -> Unit) {
        viewModelScope.launch {
            val token = getAccessTokenUseCase()
            onResult(token)
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