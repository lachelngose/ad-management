package dev.lachelngose.adManagement.domain.payment.model

import dev.lachelngose.adManagement.domain.campaign.model.Campaign
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
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
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    val status: PaymentStatus,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null
}
