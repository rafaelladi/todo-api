package com.dietrich.futsal.model.security

import java.util.UUID

data class Token(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer"
)

data class TokenUser(
    val id: UUID,
    val email: String
)
