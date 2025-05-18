package com.vertyll.projectabackend.auth.model

import com.vertyll.projectabackend.common.entity.BaseEntity
import com.vertyll.projectabackend.user.model.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "refresh_token")
class RefreshToken(
    @Column(nullable = false, unique = true)
    var token: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,
    @Column(nullable = false)
    var expiryDate: Instant,
    @Column(nullable = false)
    var revoked: Boolean = false,
    @Column(nullable = true)
    var deviceInfo: String? = null,
) : BaseEntity()
