package com.vertyll.projectabackend.email.enums

enum class EmailTemplateName(val templateName: String) {
    ACTIVATE_ACCOUNT("activate_account"),
    CHANGE_EMAIL("change_email"),
    CHANGE_PASSWORD("change_password"),
    RESET_PASSWORD("reset_password"),
}
