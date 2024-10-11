package dev.lachelngose.adManagement.domain.campaign.model

import dev.lachelngose.adManagement.domain.campaign.model.converter.AdTargetConverter
import dev.lachelngose.adManagement.domain.payment.model.PaymentStatus
import dev.lachelngose.adManagement.domain.user.model.Customer
import jakarta.persistence.*
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
    lateinit var updatedAt: LocalDateTime

    var deletedAt: LocalDateTime? = null

    var isDeleted: Boolean = false
}
