package dev.lachelngose.adManagement.controller.dto

import dev.lachelngose.adManagement.resolver.UserInfo

data class PaymentRequest(
    val amount: Double,
    val paymentInfoId: Long,
    val campaignId: Long,
) {
    lateinit var userInfo: UserInfo
}