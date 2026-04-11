package com.vertyll.kotlinapi.role.enums

enum class RoleType {
    ADMIN,
    USER,
    MANAGER,
    EMPLOYEE,
    ;

    fun getAuthority(): String = "ROLE_$name"
}
