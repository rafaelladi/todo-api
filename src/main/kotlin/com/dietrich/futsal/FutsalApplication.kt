package com.dietrich.futsal

import com.dietrich.futsal.component.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties::class)
class FutsalApplication

fun main(args: Array<String>) {
    runApplication<FutsalApplication>(*args)
}
