package dev.lachelngose.adManagement.domain.payment.repository

import dev.lachelngose.adManagement.domain.payment.model.PaidHistory
import org.springframework.data.jpa.repository.JpaRepository

interface PaidHistoryRepository : JpaRepository<PaidHistory, Long> {
    fun findAllByCustomerId(customerId: Long): List<PaidHistory>
}