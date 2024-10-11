package dev.lachelngose.adManagement.domain.campaign.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(columnNames = ["impression_date", "video_id"])],
    indexes = [Index(name = "idx_campaign_id_video_id", columnList = "campaign_id, video_id")]
)
class AdImpression(
    @NotNull
    val videoId: String,

    @NotNull
    val videoTitle: String,

    @NotNull
    val campaignId: Long,

    @NotNull
    val campaignName: String,

    @NotNull
    val creativeId: Long,

    @NotNull
    val creativeTitle: String,

    @NotNull
    val impressionDate: LocalDate,

    @NotNull
    val impressionCount: Long,

    @NotNull
    val viewCount: Long,

    @NotNull
    val cost: Double,

    @NotNull
    val clickCount: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_allowed_status_id")
    val videoAllowedStatus: VideoAllowedStatus? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}