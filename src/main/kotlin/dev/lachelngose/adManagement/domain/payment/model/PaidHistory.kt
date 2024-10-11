package dev.lachelngose.adManagement.domain.payment.model

import dev.lachelngose.adManagement.domain.campaign.model.Campaign
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(indexes = [Index(name = "idx_customerId_campaignId", columnList = "customer_id, campaign_id")])
class PaidHistory(
    @NotNull
    val customerId: Long,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    val campaign: Campaign,

    @NotNull
    val amount: Double,

    @NotNull
    @ManyToOne
    val paymentInfo: PaymentInfo,

    @NotNull
    val transactionId: String,

    @NotNull
    val status: PaymentStatus
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: LocalDateTime
}