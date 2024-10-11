package dev.lachelngose.adManagement.util

import dev.lachelngose.adManagement.domain.user.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtUtil {
    private val SECRET_KEY = "secret"

    fun extractUserId(token: String): Long {
        return extractClaim(token, Claims::getSubject).toLong()
    }

    fun extractRoles(token: String): List<String> {
        return extractClaim(token) { claims -> claims["roles"] as List<String> }
    }

    fun extractCustomerId(token: String): Long {
        return extractClaim(token) { claims -> claims["customerId"] as Long }
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body
    }

    fun generateToken(user: User): String {
        val claims: MutableMap<String, Any> = HashMap()
        claims["userId"] = user.id
        claims["roles"] = user.roles.map { it.name }
        claims["customerId"] = user.customer.id
        return createToken(claims, user.id.toString())
    }

    private fun createToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val userId = extractUserId(token)
        return (userId == (userDetails as User).id && !isTokenExpired(token))
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }
}