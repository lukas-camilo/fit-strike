package com.lucascamilo.fitstrike.data.repository

import com.lucascamilo.fitstrike.data.local.TokenStorage
import com.lucascamilo.fitstrike.data.remote.CognitoService
import com.lucascamilo.fitstrike.domain.model.Token
import com.lucascamilo.fitstrike.domain.repository.TokenRepository

class TokenRepositoryImpl(
    private val tokenStorage: TokenStorage,
    private val cognitoService: CognitoService
) : TokenRepository {
    override suspend fun saveToken(token: Token) {
        tokenStorage.saveToken(token.accessToken, token.refreshToken)
    }

    override suspend fun getAccessToken(): String? {
        return tokenStorage.getAccessToken()
    }

    override suspend fun refreshToken(): String {
        val refreshToken = tokenStorage.getRefreshToken()
            ?: throw IllegalStateException("Refresh token not found")
        var newAccessToken: String? = null
        cognitoService.refreshToken(refreshToken, onSuccess = {
            newAccessToken = it
        }, onError = {
            throw it
        })
        return newAccessToken ?: throw IllegalStateException("Failed to refresh token")
    }
}