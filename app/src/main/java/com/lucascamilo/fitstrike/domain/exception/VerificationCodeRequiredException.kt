package com.lucascamilo.fitstrike.domain.exception

class VerificationCodeRequiredException(val email: String) : RuntimeException(
    "Verification code is required for the user with email: $email"
)