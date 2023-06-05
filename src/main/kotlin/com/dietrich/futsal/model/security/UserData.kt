package com.dietrich.futsal.model.security

import java.util.UUID

data class UserData(
    val id: UUID,
    val email: String
)
