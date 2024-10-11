package dev.lachelngose.adManagement.domain.campaign.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(indexes = [Index(name = "idx_campaignId_videoId", columnList = "campaign_id, video_id")])
class VideoAllowedStatus(
    @NotNull
    @Column(unique = true)
    val videoId: String,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val campaign: Campaign,

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    var status: VideoStatus,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var updatedBy: Long? = null

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: LocalDateTime
}

enum class VideoStatus {
    ALLOWED,
    BLOCKED
}