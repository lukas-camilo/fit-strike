package com.lucascamilo.fitstrike.domain.repository

import com.lucascamilo.fitstrike.domain.model.LoginRequest
import com.lucascamilo.fitstrike.domain.model.RegisterRequest
import com.lucascamilo.fitstrike.domain.model.Token

interface AuthRepository {
    suspend fun login(request: LoginRequest): Token
    suspend fun register(request: RegisterRequest): Any
    suspend fun confirmationCode(email: String, confirmationCode: String): Any
}