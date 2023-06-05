package com.dietrich.futsal.config

import com.dietrich.futsal.model.Tags
import com.dietrich.futsal.model.Todos
import com.dietrich.futsal.model.TodosTags
import com.dietrich.futsal.model.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class ExposedConfig(dataSource: DataSource) {

    init {
        val db = Database.connect(dataSource)
        transaction(db) {
            SchemaUtils.create(Users, Tags, Todos, TodosTags)
        }
    }

}