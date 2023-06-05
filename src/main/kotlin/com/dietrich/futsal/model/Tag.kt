package com.dietrich.futsal.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Tags : UUIDTable("tags") {
    val name = varchar("name", 50)
    val user = reference("user", Users)
}

class Tag(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Tag>(Tags)

    var name by Tags.name
    var user by User referencedOn Tags.user
}

data class TagDTO(
    val id: UUID,
    val name: String,
    val userId: UUID
)

data class CreateTagDTO(
    val name: String
)

data class UpdateTagDTO(
    val name: String
)

fun Tag.toDTO(userId: UUID): TagDTO {
    return TagDTO(id.value, name, userId)
}