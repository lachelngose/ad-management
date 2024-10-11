package dev.lachelngose.adManagement.domain.campaign.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import dev.lachelngose.adManagement.controller.dto.CampaignSummary
import dev.lachelngose.adManagement.domain.campaign.model.Campaign
import dev.lachelngose.adManagement.domain.campaign.model.QAdImpression.adImpression
import dev.lachelngose.adManagement.domain.campaign.model.QCampaign.campaign
import dev.lachelngose.adManagement.domain.campaign.model.QVideoAllowedStatus.videoAllowedStatus
import dev.lachelngose.adManagement.domain.campaign.model.VideoStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface CampaignRepository: JpaRepository<Campaign, Long>, CampaignRepositoryCustom {
    fun findOneById(id: Long): Campaign?

    fun findOneByName(campaignName: String): Campaign?

    fun findAllByCustomerId(customerId: Long): List<Campaign>
}

interface CampaignRepositoryCustom {
    fun findCampaignSummary(startDate: LocalDateTime?, endDate: LocalDateTime?, isAllowed: Boolean?, campaignId: Long?): List<CampaignSummary>
}

@Repository
class CampaignRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CampaignRepositoryCustom {

    override fun findCampaignSummary(startDate: LocalDateTime?, endDate: LocalDateTime?, isAllowed: Boolean?, campaignId: Long?): List<CampaignSummary> {
        val cpv = adImpression.cost.sum().divide(adImpression.viewCount.sum().nullif(0L)).coalesce(0.0).`as`("cpv")
        val cpc = adImpression.clickCount.sum().divide(adImpression.cost.sum().nullif(0.0)).coalesce(0L).`as`("cpc")
        val allowedVideoCount = videoAllowedStatus.status.count().`as`("allowedVideoCount")
        val blockedVideoCount = videoAllowedStatus.status.count().`as`("blockedVideoCount")

        val query = queryFactory.from(campaign)
            .leftJoin(adImpression).on(adImpression.campaignId.eq(campaign.id))
            .leftJoin(videoAllowedStatus.campaign, campaign)
            .groupBy(adImpression.campaignId)
            .select(
                Projections.constructor(
                    CampaignSummary::class.java,
                    campaign.name.`as`("campaignName"),
                    campaign.startDate.`as`("startDate"),
                    campaign.endDate.`as`("endDate"),
                    adImpression.cost.sum().`as`("cost"),
                    adImpression.impressionCount.sum().`as`("impressionCount"),
                    adImpression.viewCount.sum().`as`("viewCount"),
                    cpv,
                    cpc,
                    allowedVideoCount,
                    blockedVideoCount
                )
            )

        campaignId?.let { query.where(campaign.id.eq(it)) }
        startDate?.let { query.where(campaign.startDate.after(it)) }
        endDate?.let { query.where(campaign.endDate.before(it)) }
        isAllowed?.let {
            query.where(
                if (it) videoAllowedStatus.status.eq(VideoStatus.ALLOWED)
                else videoAllowedStatus.status.eq(VideoStatus.BLOCKED)
            )
        }

        return query.fetch()
    }
}