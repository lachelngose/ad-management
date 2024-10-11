package dev.lachelngose.adManagement.error.exception

import dev.lachelngose.adManagement.error.ErrorType

data class ServiceException(
    override var message: String,
    val code: String,
) : RuntimeException(message)

fun ErrorType.toServiceException(message: String? = null): Throwable {
    return ServiceException(code = this.name, message = message ?: this.message)
}