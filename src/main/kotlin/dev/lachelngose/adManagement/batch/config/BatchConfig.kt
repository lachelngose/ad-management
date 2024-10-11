package dev.lachelngose.adManagement.batch.config

import dev.lachelngose.adManagement.batch.jobs.GenerateAdImpressionTasklet
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
@EnableScheduling
class BatchConfig(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val jobLauncher: JobLauncher,
    private val generateAdImpressionTasklet: GenerateAdImpressionTasklet
) {

    @Bean
    fun generateAdImpressionJob(): Job {
        return JobBuilder("generateAdImpressionJob", jobRepository)
            .start(generateAdImpressionStep())
            .build()
    }

    @Bean
    fun generateAdImpressionStep(): Step {
        return StepBuilder("generateAdImpressionStep", jobRepository)
            .tasklet(generateAdImpressionTasklet, transactionManager)
            .build()
    }

    @Scheduled(cron = "0 0 * * * ?")
    fun runGenerateAdImpressionJob() {
        jobLauncher.run(generateAdImpressionJob(), org.springframework.batch.core.JobParameters())
    }
}