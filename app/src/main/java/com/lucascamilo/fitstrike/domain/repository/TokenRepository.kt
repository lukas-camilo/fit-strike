package com.lucascamilo.fitstrike.domain.repository

import com.lucascamilo.fitstrike.domain.model.Token

interface TokenRepository {
    suspend fun saveToken(token: Token)
    suspend fun getAccessToken(): String?
    suspend fun refreshToken(): String
}