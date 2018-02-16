package de.hits.jobinstance.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.hits.jobinstance.common.utils.NumberUtils;
import de.hits.jobinstance.common.utils.RestPreconditions;
import de.hits.jobinstance.common.utils.SimpleManagingCache;
import de.hits.jobinstance.common.utils.StringUtils;
import de.hits.jobinstance.data.JobInstanceJobJson;
import de.hits.jobinstance.data.JobInstanceStatusJson;
import de.hits.jobinstance.domain.Catalog;
import de.hits.jobinstance.service.CatalogService;
import de.hits.jobinstance.service.JobInstanceService;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@RestController
@RequestMapping("/job/api/job")
@PropertySources({
	// default configuration file
	@PropertySource("classpath:config/manage.properties"),
	// replacing classpath configuration file by external resource file if exists
	@PropertySource(value = "file:./manage.properties", ignoreResourceNotFound = true)
})
public class JobInstanceJobController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final String CACHE_WORK_MANAGE_LIFETIME		= "manage.cache.work.managing.lifetime.max";
	private static final String CACHE_WORK_MANAGE_INTERVAL		= "manage.cache.work.managing.timeinterval";
	private static final String CACHE_WORK_LOGGING				= "manage.cache.work.logging";
	private static final String CACHE_WORK_MONITORING				= "manage.cache.work.monitoring";
	private static final String CACHE_WORK_MONITORING_INTERVAL	= "manage.cache.work.monitoring.timeinterval";
	private static final String CACHE_WRITE						= "manage.cache.write";
	private static final String CACHE_WRITE_INTERVAL				= "manage.cache.write.timeinterval";

	private final AtomicLong jobInstanceIdSequence = new AtomicLong();

	private final AtomicLong counterCreated = new AtomicLong(0);
	private final AtomicLong counterSaved = new AtomicLong(0);

	private Map<Integer, Catalog> catalogCache;
	private SimpleManagingCache<Long, JobInstanceJobJson> workCache;
	private ConcurrentHashMap<Long, JobInstanceJobJson> writeCache;

	private boolean useWriteCache = false;
	private long writeTimeInterval = -1l;

	@Autowired
	private JobInstanceService jobService;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private JobInstanceStatusController jobInstanceController;

	@Autowired
	protected Environment env;

	@PostConstruct
	private void init() throws Exception {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#init()");
		}

		String workCacheLifetimeStr = env.getProperty(CACHE_WORK_MANAGE_LIFETIME);
		String workCacheIntervalStr = env.getProperty(CACHE_WORK_MANAGE_INTERVAL);
		String workCacheLoggingStr = env.getProperty(CACHE_WORK_LOGGING);
		String workCacheMonitoringStr = env.getProperty(CACHE_WORK_MONITORING);
		String workCacheMonitoringIntervalStr = env.getProperty(CACHE_WORK_MONITORING_INTERVAL);
		String writeCacheStr = env.getProperty(CACHE_WRITE);
		String writeCacheIntervalStr = env.getProperty(CACHE_WRITE_INTERVAL);

		this.catalogCache = new HashMap<>();

		Integer workCacheLifetime = NumberUtils.getInteger(workCacheLifetimeStr);
		Integer workCacheInterval = NumberUtils.getInteger(workCacheIntervalStr);
		int timeToLive = workCacheLifetime != null ? workCacheLifetime : 3600;
		int managingTimerInterval = workCacheInterval != null ? workCacheInterval : 60;
		this.workCache = new SimpleManagingCache<>(timeToLive, managingTimerInterval);

		Boolean useWorkCacheLogging = StringUtils.getNullSaveBoolean(workCacheLoggingStr);
		if (useWorkCacheLogging != null && useWorkCacheLogging) {
			this.workCache.setLogging(true);
		}

		Boolean useWorkCacheMonitoring = StringUtils.getNullSaveBoolean(workCacheMonitoringStr);
		if (useWorkCacheMonitoring != null && useWorkCacheMonitoring) {
			Long workCacheMonitoringInterval = NumberUtils.getLong(workCacheMonitoringIntervalStr);
			if (workCacheMonitoringInterval != null) {
				this.workCache.setMonitoring(true, workCacheMonitoringInterval);
			} else {
				this.workCache.setMonitoring(true, managingTimerInterval);
			}
		}

		Boolean useWriteCache = StringUtils.getNullSaveBoolean(writeCacheStr);
		if (useWriteCache != null && useWriteCache) {
			this.useWriteCache = true;

			this.writeCache = new ConcurrentHashMap<>(100000);

			this.writeTimeInterval = NumberUtils.getNullSaveLong(writeCacheIntervalStr);
			runPersistWriteCache();
		}

		this.jobInstanceIdSequence.set(jobService.getMaxJobId());

		this.log.info("Maximum JobInstance-ID in the database: " + this.jobInstanceIdSequence.get());
		this.log.info("Use write cache: " + this.useWriteCache);
		if (this.useWriteCache) {
			this.log.info(String.format("Persist write cache every %s seconds.", this.writeTimeInterval));
		}

		this.catalogService.listAllCatalogs()
				.forEach(catalog -> this.catalogCache.put(catalog.getCatalogId(), catalog));
	}

	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	@ResponseBody
	public JobInstanceJobJson findOne(@PathVariable("id") long id) throws IOException {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#findOne()");
		}

		counterCreated.getAndIncrement();

		JobInstanceJobJson json = null;

		JobInstanceJobJson processedJob = this.workCache.get(id);
		if (processedJob != null) {
			json = processedJob;
		} else {
			// "Redirect", da diese Methode bereits existiert.
			json = this.jobInstanceController.findOne(id, true, true);
		}

		RestPreconditions.checkFound(json);

		return json;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public JobInstanceJobJson create(@RequestBody JobInstanceStatusJson resource) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#create()");
		}

		JobInstanceJobJson response = null;
		long jobInstanceId = this.jobInstanceIdSequence.incrementAndGet();

		boolean isCommonValid = !StringUtils.isEmpty(resource.getProjectName())
				&& !StringUtils.isEmpty(resource.getJobName());
		boolean isTechnicalValid = !StringUtils.isEmpty(resource.getHostName())
				&& !StringUtils.isEmpty(resource.getHostUser()) && !NumberUtils.isEmpty(resource.getHostPid());

		if (isCommonValid && isTechnicalValid) {
			response = this.jobService.createJobInstance(jobInstanceId, resource);

			this.workCache.put(jobInstanceId, response);
		}

		return response;
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public JobInstanceJobJson save(@PathVariable("id") long id, @RequestBody JobInstanceJobJson resource) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#save()");
		}

		counterSaved.getAndIncrement();

		long jobInstanceId = resource.getCurrentJob().getJobInstanceId();
		// little check
		if (jobInstanceId == id && this.workCache.containsKey(jobInstanceId)) {
			if (this.useWriteCache) {
				this.writeCache.put(jobInstanceId, resource);
			} else {
				this.jobService.persistJobInstance(resource);
			}

			// Verarbeiteten Job aus dem Cache entfernen.
			this.workCache.remove(jobInstanceId);
		}

		return resource;
	}

	@RequestMapping(value = "/previous", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public JobInstanceStatusJson previous(@RequestBody JobInstanceJobJson resource,
			@RequestParam(value = "successful", required = false) final Boolean successful,
			@RequestParam(value = "withInput", required = false) final Boolean withInput,
			@RequestParam(value = "withOutput", required = false) final Boolean withOutput,
			@RequestParam(value = "forWorkItem", required = false) final Boolean forWorkItem) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#previous()");
		}

		long jobInstanceId = resource.getCurrentJob().getJobInstanceId();
		JobInstanceJobJson cachedJob = this.workCache.get(jobInstanceId);

		// Wenn der Job im Cache existiert, soll diese Objekt verwendet und aktualisiert
		// werden.
		JobInstanceStatusJson previousJob = null;
		if (cachedJob != null) {
			cachedJob.setCurrentJob(resource.getCurrentJob());
			cachedJob.setCounters(resource.getCounters());

			this.jobService.retrievePreviousInstanceData(cachedJob, successful, withInput, withOutput, forWorkItem);
			previousJob = cachedJob.getPreviousJob();
		} else {
			this.jobService.retrievePreviousInstanceData(resource, successful, withInput, withOutput, forWorkItem);
			previousJob = resource.getPreviousJob();
		}

		return previousJob;
	}

	private void runPersistWriteCache() {
		if (log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#runPersistWriteCache()");
		}

		Thread thread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(writeTimeInterval * 1000l);
					} catch (InterruptedException ex) {
					}

					persistWriteCache();
				}
			}
		});

		thread.setDaemon(true);
		thread.start();
	}

	private void persistWriteCache() {
		if (log.isTraceEnabled()) {
			log.trace(getClass().getSimpleName() + "#persistWriteCache()");
		}

		List<Long> savedEntries = this.jobService.persistJobInstanceAll(this.writeCache);
		this.log.info(String.format("%s JobInstances persisted.", savedEntries.size()));

		savedEntries.forEach(saved -> this.writeCache.remove(saved));
		this.log.info(
				"Persisted JobInstances removed from write cache. Actual write cache size: " + this.writeCache.size());
	}
}