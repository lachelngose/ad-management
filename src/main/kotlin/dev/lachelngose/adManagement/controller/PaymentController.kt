package dev.lachelngose.adManagement.controller

import dev.lachelngose.adManagement.annotation.CurrentUser
import dev.lachelngose.adManagement.config.SwaggerConfig
import dev.lachelngose.adManagement.controller.dto.PaymentRequest
import dev.lachelngose.adManagement.controller.dto.PaymentResponse
import dev.lachelngose.adManagement.domain.payment.model.PaymentInfo
import dev.lachelngose.adManagement.domain.payment.model.PaymentMethod
import dev.lachelngose.adManagement.domain.payment.service.PaymentService
import dev.lachelngose.adManagement.resolver.UserInfo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/payments")
class PaymentController(
    private val paymentService: PaymentService
) {
    @PostMapping
    @Operation(
        summary = "결제 수단 등록",
        description = """
            로그인 정보와 결제 수단을 입력받아 결제 수단을 등록합니다.
            """,
        security = [SecurityRequirement(name = SwaggerConfig.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)],
    )
    fun createPaymentInfo(@CurrentUser user: UserInfo, paymentMethod: PaymentMethod): PaymentInfo {
        return paymentService.createPaymentInfo(user.customerId, paymentMethod)
    }

    @PostMapping("/submit")
    @Operation(
        summary = "결제 요청",
        description = """
            로그인 정보와 paymentInfo id, 결제할 campaign id, 금액을 입력 받아 결제를 진행합니다.
            """,
        security = [SecurityRequirement(name = SwaggerConfig.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)],
    )
    fun submitPayment(@CurrentUser user: UserInfo, @RequestBody request: PaymentRequest): ResponseEntity<PaymentResponse> {
        val paymentResponse = paymentService.processPayment(request.apply { userInfo = user })
        return ResponseEntity.ok(paymentResponse)
    }

    @GetMapping("/{customerId}")
    @Operation(
        summary = "결제 내역 조회",
        description = """
            고객 id 를 입력받아 결제 내역을 조회합니다.
            """,
        security = [SecurityRequirement(name = SwaggerConfig.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)],
    )
    fun getPaymentHistory(@PathVariable customerId: Long): ResponseEntity<List<PaymentResponse>> {
        val paymentHistory = paymentService.getPaymentHistory(customerId)
        return ResponseEntity.ok(paymentHistory)
    }
}