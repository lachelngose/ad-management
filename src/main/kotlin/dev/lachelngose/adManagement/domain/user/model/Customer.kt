package dev.lachelngose.adManagement.domain.user.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@SQLDelete(sql = "UPDATE customer set deleted_at = now(), is_deleted = true WHERE id = ?")
class Customer(
    @NotNull
    val name: String,
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

    var deletedAt: LocalDateTime? = null

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    var isDeleted: Boolean = false
}
