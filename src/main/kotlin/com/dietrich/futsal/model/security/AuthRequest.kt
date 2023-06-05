package com.dietrich.futsal.model.security

data class AuthRequest(
    val email: String,
    val password: String
)