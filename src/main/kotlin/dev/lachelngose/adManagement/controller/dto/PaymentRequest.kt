package dev.lachelngose.adManagement.controller.dto

data class PaymentRequest(
    val customerId: Long,
    val amount: Double,
    val paymentInfoId: Long,
    val campaignId: Long,
)