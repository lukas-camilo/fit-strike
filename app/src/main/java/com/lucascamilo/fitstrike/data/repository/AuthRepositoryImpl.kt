package com.lucascamilo.fitstrike.data.repository

import com.lucascamilo.fitstrike.data.remote.CognitoService
import com.lucascamilo.fitstrike.domain.model.LoginRequest
import com.lucascamilo.fitstrike.domain.model.RegisterRequest
import com.lucascamilo.fitstrike.domain.model.User
import com.lucascamilo.fitstrike.domain.repository.AuthRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AuthRepositoryImpl(private val cognitoService: CognitoService) : AuthRepository {
    override suspend fun login(request: LoginRequest): User {
        return suspendCancellableCoroutine { continuation ->
            cognitoService.login(
                username = request.username,
                password = request.password,
                onSuccess = { user -> continuation.resume(user) },
                onError = { error -> continuation.resumeWithException(error) }
            )
        }
    }

    override suspend fun register(request: RegisterRequest): Any {
        return suspendCancellableCoroutine { continuation ->
            cognitoService.registerUser(
                email = request.username,
                password = request.password,
                name = request.name,
                onSuccess = { message -> continuation.resume(message) },
                onError = { error -> continuation.resumeWithException(error) }
            )
        }
    }

    override suspend fun confirmationCode(email: String, confirmationCode: String): Any {
        return suspendCancellableCoroutine { continuation ->
            cognitoService.confirmUser(
                email = email,
                confirmationCode = confirmationCode,
                onSuccess = { continuation.resume(Unit) },
                onError = { error -> continuation.resumeWithException(error) }
            )
        }
    }
}