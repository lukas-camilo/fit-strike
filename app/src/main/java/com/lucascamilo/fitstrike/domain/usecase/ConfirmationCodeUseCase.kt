package com.lucascamilo.fitstrike.domain.usecase

import com.lucascamilo.fitstrike.domain.repository.AuthRepository

class ConfirmationCodeUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, confirmationCode: String): Any {
        return repository.confirmationCode(email, confirmationCode)
    }
}