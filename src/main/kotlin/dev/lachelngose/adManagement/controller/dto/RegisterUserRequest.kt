package dev.lachelngose.adManagement.controller.dto

data class RegisterUserRequest(
    val loginId: String,
    val password: String,
    val roles: Set<String>,
    val customerId: Long
)