package com.lucascamilo.fitstrike.domain.repository

import com.lucascamilo.fitstrike.domain.model.User

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUserName(): String?
    suspend fun getAccessToken(): String?
    suspend fun refreshToken(): String
}