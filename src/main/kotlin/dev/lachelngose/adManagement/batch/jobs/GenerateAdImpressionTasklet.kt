package dev.lachelngose.adManagement.batch.jobs

import dev.lachelngose.adManagement.domain.campaign.model.AdImpression
import dev.lachelngose.adManagement.domain.campaign.model.Campaign
import dev.lachelngose.adManagement.domain.campaign.repository.CampaignRepository
import dev.lachelngose.adManagement.domain.campaign.service.AdImpressionService
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class GenerateAdImpressionTasklet(
    private val campaignRepository: CampaignRepository,
    private val adImpressionService: AdImpressionService,
) : Tasklet {

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        val campaigns: List<Campaign> = campaignRepository.findAll()
        campaigns.forEach { campaign ->
            val adImpressions = (1..10).map {
                AdImpression(
                    videoId = "video-$it",
                    videoTitle = "Video Title $it",
                    campaignId = campaign.id,
                    campaignName = campaign.name,
                    creativeId = 1L,
                    creativeTitle = "Creative Title",
                    impressionDate = LocalDate.now(),
                    impressionCount = (100..1000).random().toLong(),
                    viewCount = (50..500).random().toLong(),
                    cost = (10..100).random().toDouble(),
                    clickCount = (5..50).random().toLong()
                )
            }
            adImpressionService.syncAdImpressionData(adImpressions)
        }
        return RepeatStatus.FINISHED
    }
}