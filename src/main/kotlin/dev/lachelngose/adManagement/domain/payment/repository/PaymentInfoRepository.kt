package dev.lachelngose.adManagement.domain.payment.repository

import dev.lachelngose.adManagement.domain.payment.model.PaymentInfo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentInfoRepository: JpaRepository<PaymentInfo, Long> {
    fun findByIdAndCustomer_Id(id: Long, customerId: Long): PaymentInfo?
}