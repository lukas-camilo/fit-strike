package com.lucascamilo.fitstrike.data.repository

import com.lucascamilo.fitstrike.data.local.SecurePreferences
import com.lucascamilo.fitstrike.data.remote.CognitoService
import com.lucascamilo.fitstrike.domain.model.User
import com.lucascamilo.fitstrike.domain.repository.UserRepository

class UserRepositoryImpl(
    private val tokenStorage: SecurePreferences,
    private val cognitoService: CognitoService
) : UserRepository {
    override suspend fun saveUser(user: User) {
        tokenStorage.saveUser(user)
    }

    override suspend fun getUserName(): String? {
        return tokenStorage.getUserName()
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