package com.dietrich.futsal.service

import com.dietrich.futsal.exception.NotFoundException
import com.dietrich.futsal.model.*
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.*

@Service
class TodoService(
    private val tagService: TagService
) {

    fun createTodo(createTodoDTO: CreateTodoDTO, currentUser: User) = transaction {
        val todo = Todo.new {
            title = createTodoDTO.title
            description = createTodoDTO.description
            dueDate = createTodoDTO.dueDate
            priority = createTodoDTO.priority
            this.user = currentUser
        }

        todo.toDTO(currentUser.id.value, todo.tags.map { it.toDTO(currentUser.id.value) })
    }

    fun findUserTodos(currentUser: User) = transaction {
        Todo.find {
            Todos.user eq currentUser.id
        }.map { it.toDTO(currentUser.id.value, it.tags.map { tag -> tag.toDTO(currentUser.id.value) }) }
    }

    fun deleteUserTodo(todoId: UUID, currentUser: User) = transaction {
        val todo = findUserTodo(todoId, currentUser)
        todo.delete()
    }

    fun updateUserTodo(todoId: UUID, updateTodoDTO: UpdateTodoDTO, currentUser: User) = transaction {
        val todo = findUserTodo(todoId, currentUser)
        updateTodoDTO.title?.let { todo.title = it }
        updateTodoDTO.description?.let { todo.description = it }
        updateTodoDTO.dueDate?.let { todo.dueDate = it }
        updateTodoDTO.priority?.let { todo.priority = it }
        todo.toDTO(currentUser.id.value, todo.tags.map { it.toDTO(currentUser.id.value) })
    }

    fun addTagToUserTodo(todoId: UUID, tagId: UUID, currentUser: User) = transaction {
        val todo = findUserTodo(todoId, currentUser)
        val tag = tagService.findUserTag(tagId, currentUser)

        if (todo.tags.none { it.id.value == tag.id.value }) {
            val tags = todo.tags.toMutableList()
            tags += tag
            todo.tags = SizedCollection(tags)
        }

        todo.toDTO(currentUser.id.value, todo.tags.map { it.toDTO(currentUser.id.value) })
    }

    fun removeTagFromUserTodo(todoId: UUID, tagId: UUID, currentUser: User) = transaction {
        val todo = findUserTodo(todoId, currentUser)
        val tag = tagService.findUserTag(tagId, currentUser)

        val tags = todo.tags.toMutableList()
        tags.removeIf { it.id.value == tag.id.value }

        todo.tags = SizedCollection(tags)

        todo.toDTO(currentUser.id.value, todo.tags.map { it.toDTO(currentUser.id.value) })
    }

    fun findUserTodo(todoId: UUID, user: User) = Todo.find {
        (Todos.id eq todoId) and (Todos.user eq user.id)
    }.singleOrNull() ?: throw NotFoundException("Todo", todoId)

}