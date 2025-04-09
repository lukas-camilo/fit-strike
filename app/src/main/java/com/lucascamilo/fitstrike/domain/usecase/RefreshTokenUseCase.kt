package com.lucascamilo.fitstrike.domain.usecase

import com.lucascamilo.fitstrike.domain.repository.TokenRepository

class RefreshTokenUseCase(private val repository: TokenRepository) {
    suspend operator fun invoke(): String = repository.refreshToken()
}