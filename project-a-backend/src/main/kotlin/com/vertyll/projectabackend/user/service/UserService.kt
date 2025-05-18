package com.vertyll.projectabackend.user.service

import com.vertyll.projectabackend.common.exception.ApiException
import com.vertyll.projectabackend.role.model.Role
import com.vertyll.projectabackend.role.service.RoleService
import com.vertyll.projectabackend.user.dto.UserCreateDto
import com.vertyll.projectabackend.user.dto.UserResponseDto
import com.vertyll.projectabackend.user.dto.UserUpdateDto
import com.vertyll.projectabackend.user.model.User
import com.vertyll.projectabackend.user.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleService: RoleService,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun createUser(dto: UserCreateDto): UserResponseDto {
        if (userRepository.existsByEmail(dto.email)) {
            throw ApiException("Email already exists", HttpStatus.BAD_REQUEST)
        }

        val roles = mutableSetOf<Role>()
        if (dto.roleNames.isNotEmpty()) {
            dto.roleNames.forEach { roleName ->
                roles.add(roleService.getOrCreateDefaultRole(roleName))
            }
        } else {
            roles.add(roleService.getOrCreateDefaultRole("USER"))
        }

        val user =
            User.create(
                firstName = dto.firstName,
                lastName = dto.lastName,
                email = dto.email,
                password = passwordEncoder.encode(dto.password),
                roles = roles,
                enabled = true,
            )

        val savedUser = userRepository.save(user)
        return mapToDto(savedUser)
    }

    @Transactional
    fun updateUser(
        id: Long,
        dto: UserUpdateDto,
    ): UserResponseDto {
        val user =
            userRepository.findById(id)
                .orElseThrow { ApiException("User not found", HttpStatus.NOT_FOUND) }

        dto.firstName.let { user.firstName = it }
        dto.lastName.let { user.lastName = it }

        dto.email.let {
            val updatedUser =
                User.create(
                    firstName = user.firstName,
                    lastName = user.lastName,
                    email = it,
                    password = user.password,
                    roles = user.roles,
                    enabled = user.isEnabled,
                )
            // Copy ID and other BaseEntity properties
            updatedUser.id = user.id
            return mapToDto(userRepository.save(updatedUser))
        }
    }

    fun getUserById(id: Long): UserResponseDto {
        val user =
            userRepository.findById(id)
                .orElseThrow { ApiException("User not found", HttpStatus.NOT_FOUND) }
        return mapToDto(user)
    }

    private fun mapToDto(user: User): UserResponseDto {
        return UserResponseDto(
            id = user.id ?: 0L,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.username,
            roles = user.roles.map { it.name }.toSet(),
            enabled = user.isEnabled,
        )
    }
}
