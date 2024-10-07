package dev.lachelngose.admangement.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotNull

@Entity
class User(
    @NotNull
    val username: String,
    @NotNull
    val password: String,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20)")
    @ElementCollection(fetch = FetchType.EAGER)
    val roles: Set<UserRole>,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}
