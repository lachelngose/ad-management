package dev.lachelngose.adManagement.domain.campaign.service

import dev.lachelngose.adManagement.controller.dto.CampaignSummary
import dev.lachelngose.adManagement.domain.campaign.model.AdImpression
import dev.lachelngose.adManagement.domain.campaign.model.VideoAllowedStatus
import dev.lachelngose.adManagement.domain.campaign.model.VideoStatus
import dev.lachelngose.adManagement.domain.campaign.repository.AdImpressionRepository
import dev.lachelngose.adManagement.domain.campaign.repository.CampaignRepository
import dev.lachelngose.adManagement.domain.campaign.repository.VideoAllowedStatusRepository
import dev.lachelngose.adManagement.error.ErrorType
import dev.lachelngose.adManagement.error.exception.toServiceException
import dev.lachelngose.adManagement.util.parseToLocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdImpressionService(
    private val campaignRepository: CampaignRepository,
    private val adImpressionRepository: AdImpressionRepository,
    private val videoAllowedStatusRepository: VideoAllowedStatusRepository,
) {
    @Transactional
    fun syncAdImpressionData(adImpressions: List<AdImpression>) {
        val campaignAndVideoIds = adImpressions.associate { it.campaignId to it.videoId }
        val existCampaignAndVideoIds = videoAllowedStatusRepository.findAllByCampaignIdIn(campaignAndVideoIds.keys.toList())
            .associate { it.campaign.id to it.videoId }
        val needToSaveVideoIds = campaignAndVideoIds.entries.toList().minus(existCampaignAndVideoIds.entries)

        needToSaveVideoIds.forEach {
            val campaign = campaignRepository.findOneById(it.key) ?: return
            videoAllowedStatusRepository.save(
                VideoAllowedStatus(
                    campaign = campaign,
                    videoId = it.value,
                    status = VideoStatus.ALLOWED
                )
            )
        }

        adImpressionRepository.saveAll(adImpressions)
    }

    @Transactional(readOnly = true)
    fun getAdImpressionList(campaignId: Long, customerId: Long): List<AdImpression> {
        if (!isAccessible(campaignId, customerId)) {
            throw ErrorType.UNAUTHORIZED.toServiceException()
        }

        return adImpressionRepository.findAllByCampaignId(campaignId).sortedByDescending { it.impressionDate }
    }

    @Transactional(readOnly = true)
    fun getAdImpressionListByVideoId(campaignId: Long, videoId: String, customerId: Long): List<AdImpression> {
        if (!isAccessible(campaignId, customerId)) {
            throw ErrorType.UNAUTHORIZED.toServiceException()
        }

        return adImpressionRepository.findAllByCampaignIdAndVideoId(campaignId, videoId)
            .sortedByDescending { it.impressionDate }
    }

    @Transactional(readOnly = true)
    fun getCampaignSummary(
        customerId: Long,
        startDate: String? = null,
        endDate: String? = null,
        isAllowed: Boolean? = null,
        campaignName: String? = null,
    ): List<CampaignSummary> {
        val campaignId = campaignName?.let { campaignRepository.findOneByName(it)?.id }
        if (campaignId != null && !isAccessible(campaignId, customerId)) {
            throw ErrorType.UNAUTHORIZED.toServiceException()
        }

        val startDateTime = startDate?.let { parseToLocalDateTime(it) }
        val endDateTime = endDate?.let { parseToLocalDateTime(it) }
        return campaignRepository.findCampaignSummary(startDateTime, endDateTime, isAllowed, campaignId)
    }

    private fun isAccessible(campaignId: Long, customerId: Long): Boolean {
        val campaign = campaignRepository.findOneById(campaignId) ?: return false
        return campaign.customer.id == customerId
    }
}