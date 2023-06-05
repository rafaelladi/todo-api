package com.dietrich.futsal.service

import com.dietrich.futsal.exception.NotFoundException
import com.dietrich.futsal.model.User
import com.dietrich.futsal.model.Users
import com.dietrich.futsal.model.security.AuthRequest
import com.dietrich.futsal.model.security.AuthResponse
import com.dietrich.futsal.model.security.RegisterRequest
import com.dietrich.futsal.model.security.UserData
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    fun register(request: RegisterRequest): AuthResponse {
        val createdUser = transaction {
            User.new {
                email = request.email
                pwd = passwordEncoder.encode(request.password)
            }
        }

        val token = jwtService.generateTokens(createdUser)
        return AuthResponse(
            UserData(createdUser.id.value, createdUser.email),
            token
        )
    }

    fun authenticate(request: AuthRequest): AuthResponse {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(
            request.email,
            request.password
        ))

        val user = transaction {
            User.find { Users.email eq request.email }.singleOrNull() ?: throw NotFoundException("User", request.email)
        }

        val token = jwtService.generateTokens(user)
        return AuthResponse(
            UserData(user.id.value, user.email),
            token
        )
    }

    fun refresh(refreshToken: String): AuthResponse {
        val username = jwtService.extractUsername(refreshToken)
        val user = transaction {
            User.find { Users.email eq username }.singleOrNull() ?: throw NotFoundException("User", username)
        }

        val token = jwtService.generateTokens(user)
        return AuthResponse(
            UserData(user.id.value, user.email),
            token
        )
    }
}