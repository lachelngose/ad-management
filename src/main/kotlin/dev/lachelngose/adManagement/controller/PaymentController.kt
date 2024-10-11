package dev.lachelngose.adManagement.controller

import dev.lachelngose.adManagement.controller.dto.PaymentRequest
import dev.lachelngose.adManagement.controller.dto.PaymentResponse
import dev.lachelngose.adManagement.domain.payment.model.PaymentInfo
import dev.lachelngose.adManagement.domain.payment.model.PaymentMethod
import dev.lachelngose.adManagement.domain.payment.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payments")
class PaymentController(
    private val paymentService: PaymentService
) {
    @PostMapping
    fun createPaymentInfo(customerId: Long, paymentMethod: PaymentMethod): PaymentInfo {
        return paymentService.createPaymentInfo(customerId, paymentMethod)
    }

    @PostMapping("/submit")
    fun submitPayment(@RequestBody request: PaymentRequest): ResponseEntity<PaymentResponse> {
        val paymentResponse = paymentService.processPayment(request)
        return ResponseEntity.ok(paymentResponse)
    }

    @GetMapping("/{customerId}")
    fun getPaymentHistory(@PathVariable customerId: Long): ResponseEntity<List<PaymentResponse>> {
        val paymentHistory = paymentService.getPaymentHistory(customerId)
        return ResponseEntity.ok(paymentHistory)
    }
}