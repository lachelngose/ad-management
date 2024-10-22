package dev.lachelngose.adManagement.domain.payment.model

import dev.lachelngose.adManagement.domain.user.model.Customer
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
class PaymentInfo(
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val customer: Customer,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    val paymentMethod: PaymentMethod,
    val paymentCustomerId: String, // 외부 결제 솔루션의 customer id
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
