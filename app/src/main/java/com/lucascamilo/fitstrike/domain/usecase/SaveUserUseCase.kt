package com.lucascamilo.fitstrike.domain.usecase

import com.lucascamilo.fitstrike.domain.model.User
import com.lucascamilo.fitstrike.domain.repository.UserRepository

class SaveUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User) = repository.saveUser(user)
}