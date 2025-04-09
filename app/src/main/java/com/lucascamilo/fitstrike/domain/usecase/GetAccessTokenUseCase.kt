package com.lucascamilo.fitstrike.domain.usecase

import com.lucascamilo.fitstrike.domain.repository.TokenRepository

class GetAccessTokenUseCase(private val repository: TokenRepository) {
    suspend operator fun invoke(): String? = repository.getAccessToken()
}