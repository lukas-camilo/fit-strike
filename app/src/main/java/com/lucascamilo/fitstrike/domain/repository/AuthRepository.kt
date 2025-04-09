package com.lucascamilo.fitstrike.domain.repository

import com.lucascamilo.fitstrike.domain.model.LoginRequest
import com.lucascamilo.fitstrike.domain.model.RegisterRequest
import com.lucascamilo.fitstrike.domain.model.User

interface AuthRepository {
    suspend fun login(request: LoginRequest): User
    suspend fun register(request: RegisterRequest): Any
    suspend fun confirmationCode(email: String, confirmationCode: String): Any
}