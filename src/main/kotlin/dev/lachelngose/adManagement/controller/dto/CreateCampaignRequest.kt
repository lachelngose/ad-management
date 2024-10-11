package dev.lachelngose.adManagement.controller.dto

import dev.lachelngose.adManagement.domain.campaign.model.AdTarget
import dev.lachelngose.adManagement.resolver.UserInfo

data class CreateCampaignRequest(
    val name: String,
    val startDate: String,
    val endDate: String? = null,
    val adTarget: AdTarget,
    val budget: Double,
) {
    lateinit var userInfo: UserInfo
}
