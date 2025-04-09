package com.lucascamilo.fitstrike.domain.usecase

import com.lucascamilo.fitstrike.domain.model.RegisterRequest
import com.lucascamilo.fitstrike.domain.repository.AuthRepository

class RegisterUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(request: RegisterRequest): Any {
        return repository.register(request)
    }
}