package com.dietrich.futsal.service

import com.dietrich.futsal.component.JwtProperties
import com.dietrich.futsal.model.User
import com.dietrich.futsal.model.security.Token
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtService(val jwtProperties: JwtProperties) {

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractClaims(token)
        return claimsResolver(claims)
    }

    fun generateTokens(user: User, extraClaims: Map<String, Any> = emptyMap()): Token {
        return Token(
            generateAccessToken(user, extraClaims),
            generateRefreshToken(user, extraClaims)
        )
    }

    fun generateAccessToken(user: User, extraClaims: Map<String, Any> = mapOf()) = generateTokenForExpiration(
        user,
        jwtProperties.accessExpiration,
        extraClaims
    )

    fun generateRefreshToken(user: User, extraClaims: Map<String, Any> = mapOf()) = generateTokenForExpiration(
        user,
        jwtProperties.refreshExpiration,
        extraClaims
    )

    fun generateTokenForExpiration(user: User, expirationTime: Int, extraClaims: Map<String, Any> = emptyMap()): String {
        val currentDate = Date()
        val expirationDate = Date(currentDate.time + expirationTime)

        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(user.email)
            .setIssuedAt(currentDate)
            .setExpiration(expirationDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(jwtProperties.secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

}