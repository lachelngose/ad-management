package dev.lachelngose.adManagement.domain.user.model

import jakarta.persistence.*
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
    lateinit var updatedAt: LocalDateTime

    var deletedAt: LocalDateTime? = null

    var isDeleted: Boolean = false
}
