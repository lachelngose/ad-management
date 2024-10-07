package dev.lachelngose.admangement.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtil {
    private val secretKey = "your-secret-key"

    // 토큰에서 사용자 이름 추출
    fun extractUsername(token: String): String? =
        try {
            extractClaim(token, Claims::getSubject)
        } catch (e: ExpiredJwtException) {
            null // 토큰이 만료되면 null 반환
        }

    // 토큰에서 만료 시간 추출
    fun extractExpiration(token: String): Date? =
        try {
            extractClaim(token, Claims::getExpiration)
        } catch (e: ExpiredJwtException) {
            null // 만료된 토큰은 null 반환
        }

    // 공통 메서드: 원하는 Claim 추출
    fun <T> extractClaim(
        token: String,
        claimsResolver: (Claims) -> T,
    ): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    // 토큰에서 모든 Claims 추출
    private fun extractAllClaims(token: String): Claims =
        Jwts
            .parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body

    // 토큰이 만료되었는지 확인
    fun isTokenExpired(token: String): Boolean {
        val expiration = extractExpiration(token)
        return expiration?.before(Date()) ?: true // 만료 시간이 없으면 만료된 것으로 간주
    }

    // 사용자 이름으로 토큰 생성
    fun generateToken(username: String): String {
        val claims: Map<String, Any> = HashMap()
        return createToken(claims, username)
    }

    // JWT 생성 로직
    private fun createToken(
        claims: Map<String, Any>,
        subject: String,
    ): String =
        Jwts
            .builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10시간 유효
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

    // 토큰이 유효한지 검증
    fun validateToken(
        token: String,
        userDetails: UserDetails,
    ): Boolean {
        val username = extractUsername(token)
        return (username == userDetails.username && !isTokenExpired(token))
    }

    // 인증 객체 생성
    fun getAuthenticationToken(
        token: String,
        userDetails: UserDetails,
    ): UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
}
