package com.vertyll.kotlinapi.role.model

import com.vertyll.kotlinapi.common.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "role")
data class Role(
    @Column(nullable = false, unique = true)
    val name: String,
    val description: String? = null,
) : BaseEntity()
