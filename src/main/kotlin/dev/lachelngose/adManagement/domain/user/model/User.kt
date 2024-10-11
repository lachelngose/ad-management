package dev.lachelngose.adManagement.domain.user.model

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.CascadeType
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@SQLDelete(sql = "UPDATE user set deleted_at = now(), is_deleted = true WHERE id = ?")
class User(
    @NotNull
    val username: String,

    @NotNull
    val password: String,

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    @ElementCollection(fetch = FetchType.EAGER)
    val roles: Set<UserRole>,

    @NotNull
    @ManyToOne(cascade = [CascadeType.ALL])
    val customer: Customer
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