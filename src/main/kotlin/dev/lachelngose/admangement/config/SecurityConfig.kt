package dev.lachelngose.admangement.config

import dev.lachelngose.admangement.filter.JwtRequestFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtRequestFilter: JwtRequestFilter,
    private val userDetailsService: UserDetailsService,
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager =
        authenticationConfiguration.authenticationManager

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() } // CSRF 비활성화
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/api/login", "/api/register")
                    .permitAll() // 로그인과 회원가입은 누구나 접근 가능
                    .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN") // 관리자만 접근 가능
                    .anyRequest()
                    .authenticated() // 나머지는 인증 필요
            }.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // 세션을 사용하지 않음 (JWT 사용)
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java) // JWT 필터 추가

        return http.build()
    }
}
