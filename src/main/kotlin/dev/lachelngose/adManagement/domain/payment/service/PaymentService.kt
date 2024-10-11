package dev.lachelngose.adManagement.domain.payment.service

import dev.lachelngose.adManagement.controller.dto.PaymentRequest
import dev.lachelngose.adManagement.controller.dto.PaymentResponse
import dev.lachelngose.adManagement.domain.campaign.service.CampaignService
import dev.lachelngose.adManagement.domain.payment.model.PaymentInfo
import dev.lachelngose.adManagement.domain.payment.model.PaidHistory
import dev.lachelngose.adManagement.domain.payment.model.PaymentMethod
import dev.lachelngose.adManagement.domain.payment.model.PaymentStatus
import dev.lachelngose.adManagement.domain.payment.repository.PaidHistoryRepository
import dev.lachelngose.adManagement.domain.payment.repository.PaymentInfoRepository
import dev.lachelngose.adManagement.domain.user.service.CustomerService
import dev.lachelngose.adManagement.error.ErrorType
import dev.lachelngose.adManagement.error.exception.toServiceException
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentService(
    private val customerService: CustomerService,
    private val campaignService: CampaignService,
    private val paymentInfoRepository: PaymentInfoRepository,
    private val paidHistoryRepository: PaidHistoryRepository,
) {
    fun createPaymentInfo(customerId: Long, paymentMethod: PaymentMethod): PaymentInfo {
        val customer = customerService.getCustomerById(customerId)

        val paymentInfo = PaymentInfo(
            customer = customer,
            paymentMethod = paymentMethod,
            paymentCustomerId = UUID.randomUUID().toString()
        )

        return paymentInfoRepository.save(paymentInfo)
    }

    fun processPayment(request: PaymentRequest): PaymentResponse {
        val customer = customerService.getCustomerById(request.customerId)
        val campaign = campaignService.getCampaignById(request.campaignId, customer.id)
        val paymentInfo = paymentInfoRepository.findByIdAndCustomer_Id(id = request.paymentInfoId, customerId = customer.id)
            ?: throw ErrorType.PAYMENT_INFO_NOT_FOUND.toServiceException()

        val paidHistory = PaidHistory(
            customerId = customer.id,
            campaign = campaign,
            amount = request.amount,
            paymentInfo = paymentInfo,
            transactionId = UUID.randomUUID().toString(),
            status = PaymentStatus.PAID
        )

        paidHistoryRepository.save(paidHistory)

        return PaymentResponse(
            paymentId = paidHistory.transactionId,
            customerId = request.customerId,
            amount = request.amount,
            paymentMethod = paymentInfo.paymentMethod,
            paidStatus = paidHistory.status,
            paidAt = paidHistory.createdAt
        )
    }

    fun getPaymentHistory(customerId: Long): List<PaymentResponse> {
        val paymentHistory = paidHistoryRepository.findAllByCustomerId(customerId)

        return paymentHistory.map {
            PaymentResponse(
                paymentId = it.transactionId,
                customerId = it.customerId,
                amount = it.amount,
                paymentMethod = it.paymentInfo.paymentMethod,
                paidStatus = it.status,
                paidAt = it.createdAt
            )
        }
    }
}