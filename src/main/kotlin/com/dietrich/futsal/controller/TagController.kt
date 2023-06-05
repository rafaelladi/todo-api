package com.dietrich.futsal.controller

import com.dietrich.futsal.model.CreateTagDTO
import com.dietrich.futsal.model.TagDTO
import com.dietrich.futsal.model.UpdateTagDTO
import com.dietrich.futsal.model.User
import com.dietrich.futsal.service.TagService
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
import java.util.*
import javax.sql.DataSource

@RestController
@RequestMapping("tags")
class TagController(
    private val tagService: TagService
) {

    @PostMapping
    fun createTag(@RequestBody createTagDTO: CreateTagDTO, @AuthenticationPrincipal user: User): ResponseEntity<TagDTO> {
        val tag = tagService.createTag(createTagDTO, user)
        return ResponseEntity.status(HttpStatus.CREATED).body(tag)
    }

    @GetMapping("mine")
    fun getUserTags(@AuthenticationPrincipal user: User): ResponseEntity<List<TagDTO>> {
        val userTags = tagService.findUserTags(user)
        return ResponseEntity.ok(userTags)
    }

    @DeleteMapping("{tagId}")
    fun deleteUserTag(@PathVariable tagId: UUID, @AuthenticationPrincipal user: User): ResponseEntity<Void> {
        tagService.deleteUserTag(tagId, user)
        return ResponseEntity.ok().build()
    }

    @PutMapping("{tagId}")
    fun updateUserTag(@PathVariable tagId: UUID, @RequestBody updateTagDTO: UpdateTagDTO, @AuthenticationPrincipal user: User): ResponseEntity<TagDTO> {
        val tag = tagService.updateTag(tagId, updateTagDTO, user)
        return ResponseEntity.ok(tag)
    }

}