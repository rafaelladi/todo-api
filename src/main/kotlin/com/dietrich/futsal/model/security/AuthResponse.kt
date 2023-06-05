package com.dietrich.futsal.model.security

data class AuthResponse(
    val user: UserData,
    val token: Token
)
