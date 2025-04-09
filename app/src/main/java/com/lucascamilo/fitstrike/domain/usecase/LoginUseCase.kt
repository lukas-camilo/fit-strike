package com.lucascamilo.fitstrike.domain.usecase

import com.lucascamilo.fitstrike.domain.model.LoginRequest
import com.lucascamilo.fitstrike.domain.model.Token
import com.lucascamilo.fitstrike.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(request: LoginRequest): Token {
        return repository.login(request)
    }
}