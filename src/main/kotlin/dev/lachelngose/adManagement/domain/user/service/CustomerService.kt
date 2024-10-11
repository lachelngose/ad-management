package dev.lachelngose.adManagement.domain.user.service

import dev.lachelngose.adManagement.domain.user.model.Customer
import dev.lachelngose.adManagement.domain.user.repository.CustomerRepository
import dev.lachelngose.adManagement.error.ErrorType
import dev.lachelngose.adManagement.error.exception.toServiceException
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
) {
    fun getCustomerById(customerId: Long): Customer {
        return customerRepository.findOneById(customerId) ?: throw ErrorType.CUSTOMER_NOT_FOUND.toServiceException()
    }
}