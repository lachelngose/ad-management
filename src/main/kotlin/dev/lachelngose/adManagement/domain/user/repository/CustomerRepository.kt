package dev.lachelngose.adManagement.domain.user.repository

import dev.lachelngose.adManagement.domain.user.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: JpaRepository<Customer, Long> {
    fun findOneById(id: Long): Customer?
}