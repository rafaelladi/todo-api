package com.dietrich.futsal.controller

import com.dietrich.futsal.model.CreateTodoDTO
import com.dietrich.futsal.model.TodoDTO
import com.dietrich.futsal.model.UpdateTodoDTO
import com.dietrich.futsal.model.User
import com.dietrich.futsal.service.TodoService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("todos")
class TodoController(
    private val todoService: TodoService
) {

    @PostMapping
    fun createToDo(@RequestBody createTodoDTO: CreateTodoDTO, @AuthenticationPrincipal user: User): ResponseEntity<Any> {
        val todo = todoService.createTodo(createTodoDTO, user)
        return ResponseEntity.status(HttpStatus.CREATED).body(todo)
    }

    @GetMapping("mine")
    fun getUserTodos(@AuthenticationPrincipal user: User): ResponseEntity<List<TodoDTO>> {
        val todos = todoService.findUserTodos(user)
        return ResponseEntity.ok(todos)
    }

    @DeleteMapping("{todoId}")
    fun deleteUserTodo(@PathVariable todoId: UUID, @AuthenticationPrincipal user: User): ResponseEntity<Void> {
        todoService.deleteUserTodo(todoId, user)
        return ResponseEntity.ok().build()
    }

    @PutMapping("{todoId}")
    fun updateUserTodo(@PathVariable todoId: UUID, @RequestBody updateTodoDTO: UpdateTodoDTO, @AuthenticationPrincipal user: User): ResponseEntity<TodoDTO> {
        val todo = todoService.updateUserTodo(todoId, updateTodoDTO, user)
        return ResponseEntity.ok(todo)
    }

    @PostMapping("{todoId}/tags/{tagId}")
    fun addTagToUserTodo(
        @PathVariable todoId: UUID,
        @PathVariable tagId: UUID,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<TodoDTO> {
        val todo = todoService.addTagToUserTodo(todoId, tagId, user)
        return ResponseEntity.ok(todo)
    }

    @DeleteMapping("{todoId}/tags/{tagId}")
    fun removeTagFromUserTodo(
        @PathVariable todoId: UUID,
        @PathVariable tagId: UUID,
        @AuthenticationPrincipal user: User
    ): ResponseEntity<TodoDTO> {
        val todo = todoService.removeTagFromUserTodo(todoId, tagId, user)
        return ResponseEntity.ok(todo)
    }

}