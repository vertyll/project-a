package com.vertyll.projectabackend.user.controller

import com.vertyll.projectabackend.user.dto.UserCreateDto
import com.vertyll.projectabackend.user.dto.UserResponseDto
import com.vertyll.projectabackend.user.dto.UserUpdateDto
import com.vertyll.projectabackend.user.service.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
class UserControllerTest {
    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var userController: UserController

    // Use different IDs for different tests to avoid potential issues with test independence
    private val testId = (100..999).random().toLong()

    @Test
    fun `createUser should call service and return created user`() {
        // given
        val userCreateDto =
            UserCreateDto(
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                password = "password123",
                roleNames = setOf("USER"),
            )
        val userResponseDto =
            UserResponseDto(
                id = testId,
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                roles = setOf("USER"),
                enabled = true,
            )
        `when`(userService.createUser(userCreateDto)).thenReturn(userResponseDto)

        // when
        val response = userController.createUser(userCreateDto)

        // then
        verify(userService).createUser(userCreateDto)
        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertEquals(userResponseDto, response.body?.data)
        assertEquals("User created successfully", response.body?.message)
    }

    @Test
    fun `updateUser should call service and return updated user`() {
        // given
        val id = testId
        val userUpdateDto =
            UserUpdateDto(
                firstName = "Updated",
                lastName = "User",
                email = "updated@example.com",
                roleNames = setOf("USER", "ADMIN"),
            )
        val userResponseDto =
            UserResponseDto(
                id = id,
                firstName = "Updated",
                lastName = "User",
                email = "updated@example.com",
                roles = setOf("USER", "ADMIN"),
                enabled = true,
            )
        `when`(userService.updateUser(id, userUpdateDto)).thenReturn(userResponseDto)

        // when
        val response = userController.updateUser(id, userUpdateDto)

        // then
        verify(userService).updateUser(id, userUpdateDto)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(userResponseDto, response.body?.data)
        assertEquals("User updated successfully", response.body?.message)
    }

    @Test
    fun `getUser should call service and return user`() {
        // given
        val id = testId
        val userResponseDto =
            UserResponseDto(
                id = id,
                firstName = "Test",
                lastName = "User",
                email = "test@example.com",
                roles = setOf("USER"),
                enabled = true,
            )
        `when`(userService.getUserById(id)).thenReturn(userResponseDto)

        // when
        val response = userController.getUser(id)

        // then
        verify(userService).getUserById(id)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(userResponseDto, response.body?.data)
        assertEquals("User retrieved successfully", response.body?.message)
    }
}
