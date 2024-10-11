package dev.lachelngose.adManagement.domain.user.service

import dev.lachelngose.adManagement.controller.dto.RegisterUserRequest
import dev.lachelngose.adManagement.domain.user.model.User
import dev.lachelngose.adManagement.domain.user.model.UserRole
import dev.lachelngose.adManagement.domain.user.repository.UserRepository
import dev.lachelngose.adManagement.domain.user.repository.CustomerRepository
import dev.lachelngose.adManagement.error.ErrorType
import dev.lachelngose.adManagement.error.exception.toServiceException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val customerRepository: CustomerRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun getUserById(userId: Long): User {
        return userRepository.findOneById(userId) ?: throw ErrorType.USER_NOT_FOUND.toServiceException()
    }

    fun registerUser(request: RegisterUserRequest): User {
        val customer = customerRepository.findOneById(request.customerId)
            ?: throw ErrorType.CUSTOMER_NOT_FOUND.toServiceException()

        val user = User(
            username = request.loginId,
            password = passwordEncoder.encode(request.password),
            roles = request.roles.map { UserRole.valueOf(it) }.toSet(),
            customer = customer
        )

        return userRepository.save(user)
    }
}