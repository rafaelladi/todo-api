package com.dietrich.futsal.service

import com.dietrich.futsal.model.User
import com.dietrich.futsal.model.Users
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return transaction {
            User.find { Users.email eq username }.singleOrNull()
                ?: throw UsernameNotFoundException("User not found for username or email: $username")
        }
    }
}