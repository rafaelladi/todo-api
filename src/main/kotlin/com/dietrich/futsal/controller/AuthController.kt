package com.dietrich.futsal.controller

import com.dietrich.futsal.model.security.AuthRequest
import com.dietrich.futsal.model.security.AuthResponse
import com.dietrich.futsal.model.security.RefreshRequest
import com.dietrich.futsal.model.security.RegisterRequest
import com.dietrich.futsal.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth")
class AuthController(
    val authService: AuthService
) {

    @PostMapping("register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.register(request))
    }

    @PostMapping("authenticate")
    fun authenticate(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.authenticate(request))
    }

    @PostMapping("refresh")
    fun refresh(@RequestBody request: RefreshRequest): ResponseEntity<AuthResponse> {
        return ResponseEntity.ok(authService.refresh(request.refreshToken))
    }

}