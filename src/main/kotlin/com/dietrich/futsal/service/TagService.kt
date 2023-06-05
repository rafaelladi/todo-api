package com.dietrich.futsal.service

import com.dietrich.futsal.exception.NotFoundException
import com.dietrich.futsal.model.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.*

@Service
class TagService {

    fun createTag(createTagDTO: CreateTagDTO, currentUser: User) = transaction {
        Tag.new {
            name = createTagDTO.name
            user = currentUser
        }
    }.toDTO(currentUser.id.value)

    fun findUserTags(currentUser: User) = transaction {
        Tag.find {
            Tags.user eq currentUser.id
        }.map { it.toDTO(currentUser.id.value) }
    }

    fun deleteUserTag(tagId: UUID, currentUser: User) = transaction {
        val tag = findUserTag(tagId, currentUser)
        tag.delete()
    }

    fun updateTag(tagId: UUID, updateTagDTO: UpdateTagDTO, currentUser: User) = transaction {
        val tag = findUserTag(tagId, currentUser)
        tag.name = updateTagDTO.name
        tag.toDTO(currentUser.id.value)
    }

    fun findUserTag(tagId: UUID, user: User) = Tag.find {
        (Tags.id eq tagId) and (Tags.user eq user.id)
    }.singleOrNull() ?: throw NotFoundException("Tag", tagId)

}