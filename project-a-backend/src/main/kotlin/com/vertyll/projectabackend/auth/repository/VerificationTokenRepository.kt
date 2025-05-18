package com.vertyll.projectabackend.auth.repository

import com.vertyll.projectabackend.auth.model.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface VerificationTokenRepository : JpaRepository<VerificationToken, Long> {
    fun findByToken(token: String): Optional<VerificationToken>
}
