package de.hits.jobinstance.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.hits.jobinstance.JobInstanceApi;

@Configuration
@EntityScan(basePackages = { "de.hits.jobinstance.domain" }, basePackageClasses = { JobInstanceApi.class,
		Jsr310JpaConverters.class })
@EnableJpaRepositories(basePackages = { "de.hits.jobinstance.repository" })
@EnableJpaAuditing
public class RepositoryConfiguration {

}