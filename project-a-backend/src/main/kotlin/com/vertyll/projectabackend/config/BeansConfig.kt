package com.vertyll.projectabackend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
class BeansConfig {
    @Bean
    fun auditorAware(): AuditorAware<String> = ApplicationAuditAware()
}
