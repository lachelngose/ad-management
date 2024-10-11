package dev.lachelngose.adManagement.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun parseToLocalDateTime(dateString: String): LocalDateTime {
    return LocalDate.parse(dateString, formatter).atStartOfDay()
}

fun formatLocalDateTime(dateTime: LocalDateTime): String {
    return dateTime.format(formatter)
}