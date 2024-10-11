package dev.lachelngose.adManagement.controller

import dev.lachelngose.adManagement.annotation.CurrentUser
import dev.lachelngose.adManagement.config.SwaggerConfig
import dev.lachelngose.adManagement.controller.dto.CampaignSummary
import dev.lachelngose.adManagement.controller.dto.CreateCampaignRequest
import dev.lachelngose.adManagement.domain.campaign.model.AdImpression
import dev.lachelngose.adManagement.domain.campaign.model.Campaign
import dev.lachelngose.adManagement.domain.campaign.service.AdImpressionService
import dev.lachelngose.adManagement.resolver.UserInfo
import dev.lachelngose.adManagement.domain.campaign.service.CampaignService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/campaigns")
class AdManagementController(
    private val campaignService: CampaignService,
    private val adImpressionService: AdImpressionService
) {

    @PostMapping
    @Operation(
        summary = "캠페인 생성",
        description = """
            로그인 정보와 캠페인명, 타겟(지역, 연령, 성별), 예산, 운영 기간을 입력받아 캠페인을 생성합니다.
            """,
        security = [SecurityRequirement(name = SwaggerConfig.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)],
    )
    fun createCampaign(
        @CurrentUser user: UserInfo,
        @RequestBody campaignRequest: CreateCampaignRequest
    ): ResponseEntity<Campaign> {
        val createdCampaign = campaignService.createCampaign(campaignRequest.apply { userInfo = user })
        return ResponseEntity.ok(createdCampaign)
    }

    @GetMapping
    @Operation(
        summary = "캠페인 목록 조회",
        description = """
            로그인 정보를 입력받아 내 고객사(회사)에서 관리하는 캠페인 목록을 조회합니다.
            """,
        security = [SecurityRequirement(name = SwaggerConfig.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)],
    )
    fun getAllCampaigns(@CurrentUser user: UserInfo): ResponseEntity<List<Campaign>> {
        val campaigns = campaignService.getAllCampaigns(customerId = user.customerId)
        return ResponseEntity.ok(campaigns)
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "캠페인 조회",
        description = """
            로그인 정보와 캠페인 id 를 입력받아 특정 캠페인의 정보를 조회합니다.
            """,
        security = [SecurityRequirement(name = SwaggerConfig.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)],
    )
    fun getCampaignById(
        @CurrentUser user: UserInfo,
        @PathVariable id: Long
    ): ResponseEntity<Campaign> {
        val campaign = campaignService.getCampaignById(campaignId = id, customerId = user.customerId)
        return ResponseEntity.ok(campaign)
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "캠페인 삭제",
        description = """
            로그인 정보와 캠페인 id 를 입력받아 캠페인을 삭제합니다.
            관리자만 가능합니다.
            """,
        security = [SecurityRequirement(name = SwaggerConfig.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)],
    )
    fun deleteCampaign(
        @CurrentUser user: UserInfo,
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        campaignService.deleteCampaign(id, user)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}/impressions")
    @Operation(
        summary = "광고 노출 조회",
        description = """
            캠페인 id 를 입력받아 특정 캠페인의 광고 노출 성과를 조회합니다.
            """,
        security = [SecurityRequirement(name = SwaggerConfig.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)],
    )
    fun getAdImpressions(
        @CurrentUser user: UserInfo,
        @PathVariable id: Long
    ): ResponseEntity<List<AdImpression>> {
        val videoPlacements = adImpressionService.getAdImpressionList(campaignId = id, customerId = user.customerId)
        return ResponseEntity.ok(videoPlacements)
    }

    @GetMapping("/{id}/impressions/{videoId}")
    @Operation(
        summary = "특정 영상에 대한 광고 노출 성과 조회",
        description = """
            캠페인 id, 영상 id 를 입력받아 특정 영상의 광고 노출 성과를 조회합니다.
            """,
        security = [SecurityRequirement(name = SwaggerConfig.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)],
    )
    fun getAdImpressionsByVideoId(
        @CurrentUser user: UserInfo,
        @PathVariable id: Long,
        @PathVariable videoId: String
    ): ResponseEntity<List<AdImpression>> {
        val adImpression = adImpressionService.getAdImpressionListByVideoId(
            campaignId = id,
            videoId = videoId,
            customerId = user.customerId
        )
        return ResponseEntity.ok(adImpression)
    }

    @GetMapping("/summary")
    @Operation(
        summary = "캠페인 성과 집계 조회",
        description = """
            시작일, 종료일, 허용 여부, 캠페인명을 입력받아 캠페인 성과 집계 데이터를 조회합니다.
            """,
        security = [SecurityRequirement(name = SwaggerConfig.AUTHORIZATION_BEARER_SECURITY_SCHEME_NAME)],
    )
    fun getCampaignSummary(
        @CurrentUser user: UserInfo,
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?,
        @RequestParam(required = false) isAllowed: Boolean?,
        @RequestParam(required = false) campaignName: String?,
    ): ResponseEntity<List<CampaignSummary>> {
        val campaignSummary = adImpressionService.getCampaignSummary(
            customerId = user.customerId,
            startDate = startDate,
            endDate = endDate,
            isAllowed = isAllowed,
            campaignName = campaignName
        )
        return ResponseEntity.ok(campaignSummary)
    }
}