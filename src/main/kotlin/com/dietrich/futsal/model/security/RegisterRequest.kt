package com.dietrich.futsal.model.security

data class RegisterRequest(
    val email: String,
    val password: String
)