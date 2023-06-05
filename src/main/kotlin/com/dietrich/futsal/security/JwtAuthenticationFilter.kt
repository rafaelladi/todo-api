package com.dietrich.futsal.security

import com.dietrich.futsal.service.CustomUserDetailsService
import com.dietrich.futsal.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        request.getHeader("Authorization")?.let { authHeader ->
            if (authHeader.startsWith("Bearer ")) {
                val token: String = authHeader.substring(7)
                val username = jwtService.extractUsername(token)

                if (SecurityContextHolder.getContext().authentication == null) {
                    val userDetails = userDetailsService.loadUserByUsername(username)
                    if (jwtService.validateToken(token, userDetails)) {
                        val authenticationToken = UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities
                        )
                        authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = authenticationToken
                    }
                }
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.let {
            if (it.startsWith("Bearer")) {
                it.substring(7)
            } else {
                null
            }
        }
    }
}