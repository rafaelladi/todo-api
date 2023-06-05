package com.dietrich.futsal.model

import com.fasterxml.jackson.annotation.JsonFormat
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime
import java.util.UUID

object Todos : UUIDTable("todos") {
    val title = varchar("title", 128)
    val description = varchar("description", 255).nullable()
    val dueDate = datetime("due_date").nullable()
    val priority = enumeration<Priority>("priority")
    val user = reference("user", Users)
}

class Todo(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Todo>(Todos)

    var title by Todos.title
    var description by Todos.description
    var dueDate by Todos.dueDate
    var priority by Todos.priority
    var user by User referencedOn Todos.user
    var tags by Tag via TodosTags
}

object TodosTags : Table("todos_tags") {
    val todo = reference("todo", Todos)
    val tag = reference("tag", Tags)
}

data class TodoDTO(
    val title: String,
    val description: String?,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") val dueDate: LocalDateTime?,
    val priority: Priority,
    val userId: UUID,
    val tags: List<TagDTO>
)

data class CreateTodoDTO(
    val title: String,
    val description: String?,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") val dueDate: LocalDateTime?,
    val priority: Priority = Priority.LOW
)

data class UpdateTodoDTO(
    val title: String?,
    val description: String?,
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") val dueDate: LocalDateTime?,
    val priority: Priority?
)

fun Todo.toDTO(userId: UUID, tags: List<TagDTO>) = TodoDTO(
    title,
    description,
    dueDate,
    priority,
    userId,
    tags
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}