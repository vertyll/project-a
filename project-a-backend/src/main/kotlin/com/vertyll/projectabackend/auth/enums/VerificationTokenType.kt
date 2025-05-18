package com.vertyll.projectabackend.auth.enums

/**
 * Enum representing different types of verification tokens.
 * Used to distinguish between different verification processes.
 */
enum class VerificationTokenType {
    ACCOUNT_ACTIVATION,
    EMAIL_CHANGE,
    PASSWORD_CHANGE,
    PASSWORD_RESET,
}
