package dev.lachelngose.adManagement.domain.user.service

import dev.lachelngose.adManagement.domain.user.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findOneById(username.toLong())
            ?: throw UsernameNotFoundException("User not found")

        val authorities: List<GrantedAuthority> = user.roles.map { SimpleGrantedAuthority(it.name) }

        return User(
            user.id.toString(),
            user.password,
            authorities
        )
    }
}