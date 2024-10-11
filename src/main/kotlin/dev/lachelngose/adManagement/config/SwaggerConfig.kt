package dev.lachelngose.adManagement.config

import dev.lachelngose.adManagement.config.SwaggerConfig.Companion.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun customOpenAPI(): OpenAPI =
        OpenAPI()
            .components(
                Components().addSecuritySchemes(
                    AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME,
                    AuthorizationBearerSecurityScheme,
                )
            )
            .info(
                Info()
                    .title("API Documentation")
                    .version("1.0.0")
                    .description("API documentation for the project"),
            )
    companion object {
        const val AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME = "Authorization: Bearer ACCESS_TOKEN"
    }
}

val AuthorizationBearerSecurityScheme: SecurityScheme = SecurityScheme()
    .name(AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)
    .type(SecurityScheme.Type.HTTP)
    .`in`(SecurityScheme.In.HEADER)
    .scheme("Bearer")
    .bearerFormat("JWT")
