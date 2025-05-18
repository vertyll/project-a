package com.vertyll.projectabackend.auth.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    private val clock: Clock = Clock.systemUTC(),
) {
    @Value("\${security.jwt.secret-key}")
    private lateinit var secretKey: String

    @Value("\${security.jwt.access-token-expiration}")
    private var accessTokenExpiration: Long = 0

    @Value("\${security.jwt.refresh-token-expiration}")
    private var refreshTokenExpiration: Long = 0

    @Value("\${security.jwt.refresh-token-cookie-name}")
    private lateinit var refreshTokenCookieName: String

    fun extractUsername(token: String): String {
        return extractClaim(token) { it.subject }
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), userDetails)
    }

    fun generateToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
    ): String {
        val now = Instant.now(clock)
        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(accessTokenExpiration, ChronoUnit.MILLIS)))
            .signWith(getSigningKey())
            .compact()
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        return generateRefreshToken(emptyMap(), userDetails)
    }

    fun generateRefreshToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
    ): String {
        val now = Instant.now(clock)
        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(refreshTokenExpiration, ChronoUnit.MILLIS)))
            .signWith(getSigningKey())
            .compact()
    }

    fun getRefreshTokenCookieName(): String {
        return refreshTokenCookieName
    }

    fun getRefreshTokenExpirationTime(): Long {
        return refreshTokenExpiration
    }

    fun isTokenValid(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        return try {
            val username = extractUsername(token)
            username == userDetails.username && !isTokenExpired(token)
        } catch (_: Exception) {
            false
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date.from(Instant.now(clock)))
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { it.expiration }
    }

    fun <T> extractClaim(
        token: String,
        claimsResolver: (Claims) -> T,
    ): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return try {
            Jwts.parser()
                .verifyWith(getVerificationKey())
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            e.claims
        }
    }

    private fun getSigningKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun getVerificationKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}
