package com.vertyll.projectabackend.role.enums

enum class RoleType {
    ADMIN,
    USER,
    MANAGER,
    EMPLOYEE,
    ;

    fun getAuthority(): String {
        return "ROLE_$name"
    }
}
