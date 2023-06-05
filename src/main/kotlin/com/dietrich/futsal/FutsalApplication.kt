package com.dietrich.futsal

import com.dietrich.futsal.component.JwtProperties
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@SpringBootApplication
@OpenAPIDefinition
@EnableConfigurationProperties(JwtProperties::class)
class FutsalApplication

fun main(args: Array<String>) {
    runApplication<FutsalApplication>(*args)
}
