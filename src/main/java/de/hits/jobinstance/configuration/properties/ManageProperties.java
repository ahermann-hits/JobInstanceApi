package de.hits.jobinstance.configuration.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for external configuration properties.<br>
 * <br>
 * 
 * This configuration is bound to the property file
 * '/var/data/JobInstance/api/manage-&lt;profile&gt;.properties'.
 * 
 * @author André Hermann
 * @since 17.05.2018
 * @version 1.0
 */
@Configuration
@PropertySource(value = "file:/var/data/JobInstance/api/manage-${spring.profiles.active}.properties", ignoreResourceNotFound = false)
@ConfigurationProperties
public class ManageProperties {

	@Value("${manage.cache.work.managing.lifetime.max:3600}")
	private long cacheManagingMaxLifetime;
	@Value("${manage.cache.work.managing.timeinterval:60}")
	private long cacheManagingTimeInterval;
	@Value("${manage.cache.work.logging:true}")
	private boolean cacheLoggingEnabled;
	@Value("${manage.cache.work.monitoring:true}")
	private boolean cacheMonitoringEnabled;
	@Value("${manage.cache.work.monitoring.timeinterval:60}")
	private long cacheMonitoringTimeInterval;
	@Value("${manage.cache.write:true}")
	private boolean writeCacheEnabled;
	@Value("${manage.cache.write.timeinterval:60}")
	private long writeCacheTimeInterval;
	@Value("${manage.methode.job.save.acceptnoncached:true}")
	private boolean saveNonCachedJobs;

	/**
	 * @return the value of the property 'manage.cache.work.managing.lifetime.max'
	 *         from the configuration file, or the default value '3600' if not set.
	 */
	public long getCacheManagingMaxLifetime() {
		return cacheManagingMaxLifetime;
	}

	/**
	 * @return the value of the property 'manage.cache.work.managing.timeinterval'
	 *         from the configuration file, or the default value '60' if not set.
	 */
	public long getCacheManagingTimeInterval() {
		return cacheManagingTimeInterval;
	}

	/**
	 * @return the value of the property 'manage.cache.work.logging' from the
	 *         configuration file, or the default value 'true' if not set.
	 */
	public boolean isCacheLoggingEnabled() {
		return cacheLoggingEnabled;
	}

	/**
	 * @return the value of the property 'manage.cache.work.monitoring' from the
	 *         configuration file, or the default value 'true' if not set.
	 */
	public boolean isCacheMonitoringEnabled() {
		return cacheMonitoringEnabled;
	}

	/**
	 * @return the value of the property 'manage.cache.work.monitoring.timeinterval'
	 *         from the configuration file, or the default value '60' if not set.
	 */
	public long getCacheMonitoringTimeInterval() {
		return cacheMonitoringTimeInterval;
	}

	/**
	 * @return the value of the property 'manage.cache.write' from the configuration
	 *         file, or the default value 'true' if not set.
	 */
	public boolean isWriteCacheEnabled() {
		return writeCacheEnabled;
	}

	/**
	 * @return the value of the property 'manage.cache.write.timeinterval' from the
	 *         configuration file, or the default value '60' if not set.
	 */
	public long getWriteCacheTimeInterval() {
		return writeCacheTimeInterval;
	}

	/**
	 * @return the value of the property 'manage.methode.job.save.acceptnoncached'
	 *         from the configuration file, or the default value 'true' if not set.
	 */
	public boolean getSaveNonCachedJobs() {
		return saveNonCachedJobs;
	}
}