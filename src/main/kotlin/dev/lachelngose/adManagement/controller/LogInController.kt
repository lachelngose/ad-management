package dev.lachelngose.adManagement.controller

import dev.lachelngose.adManagement.controller.dto.LogInRequest
import dev.lachelngose.adManagement.controller.dto.RegisterUserRequest
import dev.lachelngose.adManagement.controller.dto.TokenResponse
import dev.lachelngose.adManagement.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class LogInController(
    private val userService: UserService
) {
    @PostMapping("/register")
    @Operation(
        summary = "회원 가입",
        description = """
            login id, role, customer id 를 입력받아 회원 가입을 처리합니다.
            """
    )
    fun registerUser(@RequestBody request: RegisterUserRequest): ResponseEntity<TokenResponse> {
        val token = userService.registerUser(request)
        return ResponseEntity.ok(token)
    }

    @PostMapping("/login")
    @Operation(
        summary = "로그인",
        description = """
            login id, password 를 입력받아 로그인합니다.
            """
    )
    fun loginUser(@RequestBody request: LogInRequest): ResponseEntity<TokenResponse> {
        val token = userService.loginUser(request.loginId, request.password)
        return ResponseEntity.ok(token)
    }
}