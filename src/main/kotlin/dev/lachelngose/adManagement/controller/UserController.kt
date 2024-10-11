package dev.lachelngose.adManagement.controller

import dev.lachelngose.adManagement.controller.dto.RegisterUserRequest
import dev.lachelngose.adManagement.domain.user.service.UserService
import dev.lachelngose.adManagement.domain.user.model.User
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterUserRequest): ResponseEntity<User> {
        val user = userService.registerUser(request)
        return ResponseEntity.ok(user)
    }
}