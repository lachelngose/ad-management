package dev.lachelngose.adManagement.domain.user.service

import dev.lachelngose.adManagement.controller.dto.RegisterUserRequest
import dev.lachelngose.adManagement.controller.dto.TokenResponse
import dev.lachelngose.adManagement.domain.user.model.User
import dev.lachelngose.adManagement.domain.user.model.UserRole
import dev.lachelngose.adManagement.domain.user.repository.UserRepository
import dev.lachelngose.adManagement.domain.user.repository.CustomerRepository
import dev.lachelngose.adManagement.error.ErrorType
import dev.lachelngose.adManagement.error.exception.toServiceException
import dev.lachelngose.adManagement.util.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val customerRepository: CustomerRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    fun getUserById(userId: Long): User {
        return userRepository.findOneById(userId) ?: throw ErrorType.USER_NOT_FOUND.toServiceException()
    }

    fun registerUser(request: RegisterUserRequest): TokenResponse {
        val customer = customerRepository.findOneById(request.customerId)
            ?: throw ErrorType.CUSTOMER_NOT_FOUND.toServiceException()

        val user = User(
            username = request.loginId,
            password = passwordEncoder.encode(request.password),
            roles = request.roles.map { UserRole.valueOf(it) }.toSet(),
            customer = customer
        )

        return generateSignInResult(userRepository.save(user))
    }

    fun loginUser(loginId: String, password: String): TokenResponse {
        val user = userRepository.findOneByUsername(loginId)
            ?: throw ErrorType.USER_NOT_FOUND.toServiceException()

        if (!passwordEncoder.matches(password, user.password)) {
            throw ErrorType.LOGIN_DENIED.toServiceException()
        }

        return generateSignInResult(user)
    }

    private fun generateSignInResult(user: User): TokenResponse {
        return try {
            val token = jwtUtil.generateToken(user)
            TokenResponse(accessToken = token)
        } catch (e: Exception) {
            throw ErrorType.UNAUTHORIZED.toServiceException()
        }
    }
}