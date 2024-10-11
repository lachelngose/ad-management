package dev.lachelngose.adManagement.domain.campaign.model

data class AdTarget(
    val region: MutableList<String>,
    val gender: MutableList<Gender>,
    val age: MutableList<TargetAges>,
)

enum class TargetAges {
    TEEN,
    YOUNG_ADULT,
    ADULT,
    SENIOR,
}

enum class Gender {
    MALE,
    FEMALE,
}
