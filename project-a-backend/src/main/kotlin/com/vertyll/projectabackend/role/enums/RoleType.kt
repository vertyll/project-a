package com.vertyll.projectabackend.role.enums

enum class RoleType {
    ADMIN,
    USER,
    MANAGER,
    EMPLOYEE,
    ;

    fun getAuthority(): String = "ROLE_$name"
}
