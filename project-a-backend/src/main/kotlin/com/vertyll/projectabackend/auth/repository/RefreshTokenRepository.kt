package com.vertyll.projectabackend.auth.repository

import com.vertyll.projectabackend.auth.model.RefreshToken
import com.vertyll.projectabackend.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.Optional

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): Optional<RefreshToken>

    fun findAllByUser(user: User): List<RefreshToken>

    fun findByUserAndRevoked(
        user: User,
        revoked: Boolean,
    ): List<RefreshToken>

    fun existsByToken(token: String): Boolean

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user = :user")
    fun revokeAllUserTokens(user: User)

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.id = :id")
    fun revokeById(id: Long)

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiryDate < :now")
    fun deleteAllExpiredTokens(now: Instant)
}
