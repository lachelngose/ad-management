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
import org.springframework.transaction.annotation.Transactional

@Service
class CampaignService(
    private val userService: UserService,
    private val campaignRepository: CampaignRepository,
) {
    @Transactional
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

    @Transactional
    fun activateCampaign(campaignId: Long, customerId: Long): Campaign {
        val campaign = campaignRepository.findOneById(campaignId) ?: throw ErrorType.CAMPAIGN_NOT_FOUND.toServiceException()
        if (campaign.customer.id != customerId) {
            throw ErrorType.UNAUTHORIZED.toServiceException()
        }
        campaign.isActive = true
        return campaignRepository.save(campaign)
    }

    @Transactional(readOnly = true)
    fun getAllCampaigns(customerId: Long): List<Campaign> {
        return campaignRepository.findAllByCustomerId(customerId)
    }

    @Transactional(readOnly = true)
    fun getCampaignById(campaignId: Long, customerId: Long): Campaign {
        return campaignRepository.findOneById(campaignId)?.takeIf { it.customer.id == customerId }
            ?: throw ErrorType.CAMPAIGN_NOT_FOUND.toServiceException()
    }

    @Transactional
    fun deleteCampaign(id: Long, user: UserInfo) {
        return if (user.roles.contains(UserRole.ADMIN.name)) {
            campaignRepository.deleteById(id)
        } else {
            throw ErrorType.UNAUTHORIZED.toServiceException()
        }
    }
}