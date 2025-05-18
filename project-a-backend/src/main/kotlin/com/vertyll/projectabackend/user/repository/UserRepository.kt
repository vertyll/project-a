package com.vertyll.projectabackend.user.repository

import com.vertyll.projectabackend.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>

    fun existsByEmail(email: String): Boolean

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    fun findByEmailWithRoles(email: String): Optional<User>
}
