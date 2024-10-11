package dev.lachelngose.adManagement.controller.dto

data class CampaignSummary(
    val campaignName: String,
    val startDate: String,
    val endDate: String,
    val cost: Double,
    val impressionCount: Long,
    val viewCount: Long,
    val cpv: Double,
    val cpc: Double,
    val allowedVideoCount: Long,
    val blockedVideoCount: Long,
)