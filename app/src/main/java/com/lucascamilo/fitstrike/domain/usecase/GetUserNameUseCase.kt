package com.lucascamilo.fitstrike.domain.usecase

import com.lucascamilo.fitstrike.domain.repository.UserRepository

class GetUserNameUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): String? = repository.getUserName()
}