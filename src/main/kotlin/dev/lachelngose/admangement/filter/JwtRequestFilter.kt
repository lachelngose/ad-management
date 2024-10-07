package dev.lachelngose.admangement.filter

import dev.lachelngose.admangement.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtRequestFilter(
    private val userDetailsService: UserDetailsService,
    private val jwtUtil: JwtUtil,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authorizationHeader = request.getHeader("Authorization")
        var username: String? = null
        var jwt: String? = null

        // JWT 토큰이 있는지 확인하고 Bearer 타입의 토큰인지 확인
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7)
            username = jwtUtil.extractUsername(jwt) // JWT에서 사용자 이름 추출
        }

        // SecurityContext에 인증 정보가 없는 경우, 토큰이 유효한지 확인
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            // 토큰이 유효한 경우 인증 처리
            if (jwtUtil.validateToken(jwt!!, userDetails)) {
                val authenticationToken = jwtUtil.getAuthenticationToken(jwt, userDetails)
                authenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authenticationToken
            }
        }

        // 다음 필터로 요청을 전달
        filterChain.doFilter(request, response)
    }
}
