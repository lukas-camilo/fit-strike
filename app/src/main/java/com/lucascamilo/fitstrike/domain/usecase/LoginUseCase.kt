package com.lucascamilo.fitstrike.domain.usecase

import com.lucascamilo.fitstrike.domain.model.LoginRequest
import com.lucascamilo.fitstrike.domain.model.User
import com.lucascamilo.fitstrike.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(request: LoginRequest): User {
        return repository.login(request)
    }
}