package dev.lachelngose.adManagement.resolver

data class UserInfo(
    val userId: Long,
    val roles: List<String>,
    val customerId: Long
)
