package com.vertyll.projectabackend.role.model

import com.vertyll.projectabackend.common.entity.BaseEntity
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
