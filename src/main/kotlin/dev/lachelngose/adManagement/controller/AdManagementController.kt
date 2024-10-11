package dev.lachelngose.adManagement.controller

import dev.lachelngose.adManagement.annotation.CurrentUser
import dev.lachelngose.adManagement.controller.dto.CampaignSummary
import dev.lachelngose.adManagement.controller.dto.CreateCampaignRequest
import dev.lachelngose.adManagement.domain.campaign.model.AdImpression
import dev.lachelngose.adManagement.domain.campaign.model.Campaign
import dev.lachelngose.adManagement.resolver.UserInfo
import dev.lachelngose.adManagement.domain.campaign.service.CampaignService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/campaigns")
class AdManagementController(private val campaignService: CampaignService) {

    @PostMapping
    fun createCampaign(
        @CurrentUser user: UserInfo,
        @RequestBody campaignRequest: CreateCampaignRequest
    ): ResponseEntity<Campaign> {
        val createdCampaign = campaignService.createCampaign(campaignRequest.apply { userInfo = user })
        return ResponseEntity.ok(createdCampaign)
    }

    @GetMapping
    fun getAllCampaigns(@CurrentUser user: UserInfo): ResponseEntity<List<Campaign>> {
        val campaigns = campaignService.getAllCampaigns(customerId = user.customerId)
        return ResponseEntity.ok(campaigns)
    }

    @GetMapping("/{id}")
    fun getCampaignById(
        @CurrentUser user: UserInfo,
        @PathVariable id: Long
    ): ResponseEntity<Campaign> {
        val campaign = campaignService.getCampaignById(campaignId = id, customerId = user.customerId)
        return ResponseEntity.ok(campaign)
    }

    @DeleteMapping("/{id}")
    fun deleteCampaign(
        @CurrentUser user: UserInfo,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        campaignService.deleteCampaign(id, user)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}/impressions")
    fun getAdImpressions(
        @CurrentUser user: UserInfo,
        @PathVariable id: Long
    ): ResponseEntity<List<AdImpression>> {
        val videoPlacements = campaignService.getAdImpressionList(campaignId = id, customerId = user.customerId)
        return ResponseEntity.ok(videoPlacements)
    }

    @GetMapping("/{id}/impressions/{videoId}")
    fun getAdImpressionsByVideoId(
        @CurrentUser user: UserInfo,
        @PathVariable id: Long,
        @PathVariable videoId: String
    ): ResponseEntity<List<AdImpression>> {
        val adImpression = campaignService.getAdImpressionListByVideoId(
            campaignId = id,
            videoId = videoId,
            customerId = user.customerId
        )
        return ResponseEntity.ok(adImpression)
    }

    @GetMapping("/summary")
    fun getCampaignSummary(
        @CurrentUser user: UserInfo,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?,
        @RequestParam(required = false) isAllowed: Boolean?,
        @RequestParam(required = false) campaignName: String?,
    ): ResponseEntity<List<CampaignSummary>> {
        val campaignSummary = campaignService.getCampaignSummary(
            customerId = user.customerId,
            startDate = startDate,
            endDate = endDate,
            isAllowed = isAllowed,
            campaignName = campaignName
        )
        return ResponseEntity.ok(campaignSummary)
    }
}