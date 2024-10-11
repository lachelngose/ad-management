package dev.lachelngose.adManagement.resolver

import dev.lachelngose.adManagement.annotation.CurrentUser
import dev.lachelngose.adManagement.util.JwtUtil
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes

@Component
class CurrentUserArgumentResolver(
    private val jwtUtil: JwtUtil
) : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.getParameterAnnotation(CurrentUser::class.java) != null
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: org.springframework.web.bind.support.WebDataBinderFactory?
    ): Any? {
        val token = webRequest.getAttribute("jwtToken", RequestAttributes.SCOPE_REQUEST) as String?
        if (token != null) {
            val userId = jwtUtil.extractUserId(token)
            val roles = jwtUtil.extractRoles(token)
            val customerId = jwtUtil.extractCustomerId(token)
            return UserInfo(userId, roles, customerId)
        }
        return null
    }
}