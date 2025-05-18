package com.vertyll.projectabackend.role.repository

import com.vertyll.projectabackend.role.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Optional<Role>

    fun existsByName(name: String): Boolean
}
