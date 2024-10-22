package dev.lachelngose.adManagement.domain.campaign.model

import dev.lachelngose.adManagement.domain.campaign.model.converter.AdTargetConverter
import dev.lachelngose.adManagement.domain.payment.model.PaymentStatus
import dev.lachelngose.adManagement.domain.user.model.Customer
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@SQLDelete(sql = "UPDATE campaign set deleted_at = now(), is_deleted = true WHERE id = ?")
@Entity
class Campaign(
    @NotNull
    val name: String,

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val customer: Customer,

    @NotNull
    var startDate: LocalDateTime,

    var endDate: LocalDateTime? = null,

    @Convert(converter = AdTargetConverter::class)
    val targets: AdTarget,

    var budget: Double,

    @NotNull
    var isActive: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    var paymentStatus: PaymentStatus,

    val createdBy: Long,

    val updatedBy: Long? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @OneToMany
    var creatives: List<Creative> = mutableListOf()

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @UpdateTimestamp
    @Column(nullable = false)
    var updatedAt: LocalDateTime? = null

    var deletedAt: LocalDateTime? = null

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    var isDeleted: Boolean = false
}
