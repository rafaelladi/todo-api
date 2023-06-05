package com.dietrich.futsal.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

object Users : UUIDTable("users") {
    val email = varchar("email", 255).uniqueIndex()
    val pwd = varchar("pwd", 255)
}

class User(id: EntityID<UUID>) : UUIDEntity(id), UserDetails {
    companion object : UUIDEntityClass<User>(Users)

    var email by Users.email
    var pwd by Users.pwd

    private val authorities = listOf(SimpleGrantedAuthority("USER"))

    override fun getAuthorities() = authorities

    override fun getPassword() = pwd

    override fun getUsername() = email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}