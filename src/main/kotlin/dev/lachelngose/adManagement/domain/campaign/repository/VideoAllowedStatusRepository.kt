package dev.lachelngose.adManagement.domain.campaign.repository

import dev.lachelngose.adManagement.domain.campaign.model.VideoAllowedStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VideoAllowedStatusRepository: JpaRepository<VideoAllowedStatus, Long> {
    fun findAllByCampaignIdIn(campaignId: List<Long>): List<VideoAllowedStatus>
}