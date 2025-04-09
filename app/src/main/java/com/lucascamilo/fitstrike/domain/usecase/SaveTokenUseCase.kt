package com.lucascamilo.fitstrike.domain.usecase

import com.lucascamilo.fitstrike.domain.model.Token
import com.lucascamilo.fitstrike.domain.repository.TokenRepository

class SaveTokenUseCase(private val repository: TokenRepository) {
    suspend operator fun invoke(token: Token) = repository.saveToken(token)
}