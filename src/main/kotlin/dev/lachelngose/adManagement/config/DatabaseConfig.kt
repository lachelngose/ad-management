package dev.lachelngose.adManagement.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EntityScan(basePackages = ["dev.lachelngose.adManagement.domain"])
@EnableJpaRepositories(basePackages = ["dev.lachelngose.adManagement.domain"])
class DatabaseConfig(
    @Value("\${spring.datasource.url}") private val dbUrl: String,
    @Value("\${spring.datasource.username}") private val username: String,
    @Value("\${spring.datasource.password}") private val password: String,
    @Value("\${spring.datasource.hikari.maximum-pool-size}") private val maxPoolSize: Int,
    @Value("\${spring.datasource.hikari.minimum-idle}") private val minIdle: Int,
    @Value("\${spring.datasource.hikari.idle-timeout}") private val idleTimeout: Long,
    @Value("\${spring.datasource.hikari.max-lifetime}") private val maxLifetime: Long,
    @Value("\${spring.datasource.hikari.connection-timeout}") val connectionTimeout: Long,
    @Value("\${spring.datasource.hikari.leak-detection-threshold}") private val leakDetectionThreshold: Long,
    @Value("\${spring.datasource.driver-class-name}") private val driverClassName: String,
) {

    @Bean
    fun dataSource(): DataSource {
        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = dbUrl
        dataSource.username = username
        dataSource.password = password
        dataSource.maximumPoolSize = maxPoolSize
        dataSource.minimumIdle = minIdle
        dataSource.idleTimeout = idleTimeout
        dataSource.maxLifetime = maxLifetime
        dataSource.connectionTimeout = connectionTimeout
        dataSource.leakDetectionThreshold = leakDetectionThreshold
        dataSource.driverClassName = driverClassName
        return dataSource
    }

    @Bean
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        val vendorAdapter = HibernateJpaVendorAdapter()
        val factory = LocalContainerEntityManagerFactoryBean()
        factory.dataSource = dataSource
        factory.setPackagesToScan("dev.lachelngose.adManagement.domain")
        factory.jpaVendorAdapter = vendorAdapter
        return factory
    }

    @Bean
    fun transactionManager(entityManagerFactory: LocalContainerEntityManagerFactoryBean): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory.`object`
        return transactionManager
    }
}