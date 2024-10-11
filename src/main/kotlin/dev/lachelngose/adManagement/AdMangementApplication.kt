package dev.lachelngose.adManagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class AdManagementApplication

fun main(args: Array<String>) {
    runApplication<AdManagementApplication>(*args)
}
