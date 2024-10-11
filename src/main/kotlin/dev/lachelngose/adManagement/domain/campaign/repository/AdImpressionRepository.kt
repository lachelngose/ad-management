package dev.lachelngose.adManagement.domain.campaign.repository

import dev.lachelngose.adManagement.domain.campaign.model.AdImpression
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AdImpressionRepository: JpaRepository<AdImpression, Long> {
    fun findAllByCampaignId(campaignId: Long): List<AdImpression>

    fun findAllByCampaignIdAndVideoId(campaignId: Long, videoId: String): List<AdImpression>
}