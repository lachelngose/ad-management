package dev.lachelngose.adManagement.controller.dto

import dev.lachelngose.adManagement.domain.payment.model.PaymentMethod
import dev.lachelngose.adManagement.domain.payment.model.PaymentStatus
import java.time.LocalDateTime

data class PaymentResponse(
    val paymentId: String,
    val customerId: Long,
    val amount: Double,
    val paymentMethod: PaymentMethod,
    val paidStatus: PaymentStatus,
    val paidAt: LocalDateTime
)