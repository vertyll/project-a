package com.vertyll.projectabackend.role.service

import com.vertyll.projectabackend.common.exception.ApiException
import com.vertyll.projectabackend.role.dto.RoleCreateDto
import com.vertyll.projectabackend.role.dto.RoleResponseDto
import com.vertyll.projectabackend.role.dto.RoleUpdateDto
import com.vertyll.projectabackend.role.model.Role
import com.vertyll.projectabackend.role.repository.RoleRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoleService(
    private val roleRepository: RoleRepository,
) {
    @Transactional
    fun createRole(dto: RoleCreateDto): RoleResponseDto {
        if (roleRepository.existsByName(dto.name)) {
            throw ApiException("Role already exists", HttpStatus.BAD_REQUEST)
        }

        val role =
            Role(
                name = dto.name,
                description = dto.description,
            )

        val savedRole = roleRepository.save(role)
        return mapToDto(savedRole)
    }

    @Transactional
    fun updateRole(
        id: Long,
        dto: RoleUpdateDto,
    ): RoleResponseDto {
        val role =
            roleRepository
                .findById(id)
                .orElseThrow { ApiException("Role not found", HttpStatus.NOT_FOUND) }

        if (roleRepository.existsByName(dto.name) && role.name != dto.name) {
            throw ApiException("Role with this name already exists", HttpStatus.BAD_REQUEST)
        }

        // Create a copy with updated values since Role is a data class
        val updatedRole =
            role.copy(
                name = dto.name,
                description = dto.description,
            )

        val savedRole = roleRepository.save(updatedRole)
        return mapToDto(savedRole)
    }

    fun getOrCreateDefaultRole(roleName: String): Role =
        roleRepository
            .findByName(roleName)
            .orElseGet {
                val role =
                    Role(
                        name = roleName,
                        description = "Default role: $roleName",
                    )
                roleRepository.save(role)
            }

    fun getRoleById(id: Long): RoleResponseDto {
        val role =
            roleRepository
                .findById(id)
                .orElseThrow { ApiException("Role not found", HttpStatus.NOT_FOUND) }
        return mapToDto(role)
    }

    private fun mapToDto(role: Role): RoleResponseDto =
        RoleResponseDto(
            id = role.id ?: 0L,
            name = role.name,
            description = role.description,
        )
}
