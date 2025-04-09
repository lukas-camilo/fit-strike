package com.lucascamilo.fitstrike.domain.exception

class UsernameExistsException(val email: String) : RuntimeException(
    "Username already exists: $email"
)