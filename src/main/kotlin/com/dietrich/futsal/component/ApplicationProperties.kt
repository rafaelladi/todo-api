package com.dietrich.futsal.component

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.jwt")
class JwtProperties(
    var secret: String = "",
    var accessExpiration: Int = 0,
    var refreshExpiration: Int = 0
)