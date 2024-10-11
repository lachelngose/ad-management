package dev.lachelngose.adManagement.domain.campaign.service

import dev.lachelngose.adManagement.controller.dto.CampaignSummary
import dev.lachelngose.adManagement.controller.dto.CreateCampaignRequest
import dev.lachelngose.adManagement.domain.campaign.model.AdImpression
import dev.lachelngose.adManagement.domain.campaign.model.Campaign
import dev.lachelngose.adManagement.domain.campaign.repository.AdImpressionRepository
import dev.lachelngose.adManagement.domain.campaign.repository.CampaignRepository
import dev.lachelngose.adManagement.domain.payment.model.PaymentStatus
import dev.lachelngose.adManagement.domain.user.service.UserService
import dev.lachelngose.adManagement.domain.user.model.UserRole
import dev.lachelngose.adManagement.error.ErrorType
import dev.lachelngose.adManagement.error.exception.toServiceException
import dev.lachelngose.adManagement.resolver.UserInfo
import dev.lachelngose.adManagement.util.parseToLocalDateTime
import org.springframework.stereotype.Service

@Service
class CampaignService(
    private val userService: UserService,
    private val campaignRepository: CampaignRepository,
    private val adImpressionRepository: AdImpressionRepository
) {
    fun createCampaign(campaignRequest: CreateCampaignRequest): Campaign {
        val user = userService.getUserById(campaignRequest.userInfo.userId)

        val campaign = Campaign(
            name = campaignRequest.name,
            customer = user.customer,
            startDate = parseToLocalDateTime(campaignRequest.startDate),
            endDate = campaignRequest.endDate?.let { parseToLocalDateTime(it) },
            budget = campaignRequest.budget,
            targets = campaignRequest.adTarget,
            isActive = true,
            paymentStatus = PaymentStatus.NONE,
            createdBy = user.id
        )
        return campaignRepository.save(campaign)
    }

    fun getAllCampaigns(customerId: Long): List<Campaign> {
        return campaignRepository.findAllByCustomerId(customerId)
    }

    fun getCampaignById(campaignId: Long, customerId: Long): Campaign {
        return campaignRepository.findOneById(campaignId)?.takeIf { it.customer.id == customerId }
            ?: throw ErrorType.CAMPAIGN_NOT_FOUND.toServiceException()
    }

    fun deleteCampaign(id: Long, user: UserInfo) {
        return if (user.roles.contains(UserRole.ADMIN.name)) {
            campaignRepository.deleteById(id)
        } else {
            throw ErrorType.UNAUTHORIZED.toServiceException()
        }
    }

    fun getAdImpressionList(campaignId: Long, customerId: Long): List<AdImpression> {
        if (!isAccessible(campaignId, customerId)) {
            throw ErrorType.UNAUTHORIZED.toServiceException()
        }

        return adImpressionRepository.findAllByCampaignId(campaignId).sortedByDescending { it.impressionDate }
    }

    fun getAdImpressionListByVideoId(campaignId: Long, videoId: String, customerId: Long): List<AdImpression> {
        if (!isAccessible(campaignId, customerId)) {
            throw ErrorType.UNAUTHORIZED.toServiceException()
        }

        return adImpressionRepository.findAllByCampaignIdAndVideoId(campaignId, videoId)
            .sortedByDescending { it.impressionDate }
    }

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