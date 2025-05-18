package com.vertyll.projectabackend.auth.service

import com.vertyll.projectabackend.auth.model.RefreshToken
import com.vertyll.projectabackend.auth.repository.RefreshTokenRepository
import com.vertyll.projectabackend.common.exception.ApiException
import com.vertyll.projectabackend.user.model.User
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtService: JwtService,
    private val passwordEncoder: PasswordEncoder,
) {
    /**
     * Creates a new refresh token for the given user
     * The token is hashed before storing in the database for security
     */
    @Transactional
    fun createRefreshToken(
        user: User,
        deviceInfo: String? = null,
    ): String {
        val tokenValue = UUID.randomUUID().toString()

        val hashedToken = passwordEncoder.encode(tokenValue)

        val refreshToken =
            RefreshToken(
                token = hashedToken,
                user = user,
                expiryDate = Instant.now().plusMillis(jwtService.getRefreshTokenExpirationTime()),
                revoked = false,
                deviceInfo = deviceInfo,
            )

        refreshTokenRepository.save(refreshToken)

        return tokenValue
    }

    /**
     * Validates a refresh token and returns the associated user if valid
     */
    @Transactional(readOnly = true)
    fun validateRefreshToken(token: String): User {
        val allTokens =
            refreshTokenRepository.findAll()
                .filter { !it.revoked && it.expiryDate.isAfter(Instant.now()) }

        val refreshToken =
            allTokens.find { passwordEncoder.matches(token, it.token) }
                ?: throw ApiException("Invalid refresh token", HttpStatus.UNAUTHORIZED)

        return refreshToken.user
    }

    /**
     * Rotates a refresh token - revokes the old one and creates a new one
     * This is a security best practice to limit the lifetime of refresh tokens
     */
    @Transactional
    fun rotateRefreshToken(
        oldToken: String,
        deviceInfo: String? = null,
    ): String {
        val allTokens =
            refreshTokenRepository.findAll()
                .filter { !it.revoked && it.expiryDate.isAfter(Instant.now()) }

        val refreshToken =
            allTokens.find { passwordEncoder.matches(oldToken, it.token) }
                ?: throw ApiException("Invalid refresh token", HttpStatus.UNAUTHORIZED)

        refreshToken.revoked = true
        refreshTokenRepository.save(refreshToken)

        return createRefreshToken(refreshToken.user, deviceInfo)
    }

    /**
     * Revokes a specific refresh token
     */
    @Transactional
    fun revokeRefreshToken(token: String) {
        val allTokens =
            refreshTokenRepository.findAll()
                .filter { !it.revoked && it.expiryDate.isAfter(Instant.now()) }

        val refreshToken =
            allTokens.find { passwordEncoder.matches(token, it.token) }
                ?: return // Token not found or already revoked, nothing to do

        refreshToken.revoked = true
        refreshTokenRepository.save(refreshToken)
    }

    /**
     * Revokes all refresh tokens for a user
     */
    @Transactional
    fun revokeAllUserTokens(user: User) {
        refreshTokenRepository.revokeAllUserTokens(user)
    }

    /**
     * Gets all active sessions for a user
     */
    @Transactional(readOnly = true)
    fun getUserActiveSessions(user: User): List<RefreshToken> {
        return refreshTokenRepository.findByUserAndRevoked(user, false)
            .filter { it.expiryDate.isAfter(Instant.now()) }
    }

    /**
     * Scheduled task to delete expired tokens
     * Runs daily at midnight
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    fun cleanupExpiredTokens() {
        refreshTokenRepository.deleteAllExpiredTokens(Instant.now())
    }
}
