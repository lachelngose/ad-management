package dev.lachelngose.adManagement.domain.user.repository

import dev.lachelngose.adManagement.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findOneById(id: Long): User?
}
