package dev.lachelngose.adManagement.error

enum class ErrorType(val message: String) {
    USER_NOT_FOUND("일치하는 유저가 없습니다"),
    LOGIN_DENIED("로그인 정보가 일치하지 않습니다"),
    CUSTOMER_NOT_FOUND("일치하는 고객사가 없습니다"),
    CAMPAIGN_NOT_FOUND("캠페인을 찾을 수 없습니다"),
    UNAUTHORIZED("작업을 수행할 권한이 없습니다"),
    AD_IMPRESSION_NOT_FOUND("광고 노출 정보를 찾을 수 없습니다"),
    PAYMENT_INFO_NOT_FOUND("결제 정보를 찾을 수 없습니다"),
}