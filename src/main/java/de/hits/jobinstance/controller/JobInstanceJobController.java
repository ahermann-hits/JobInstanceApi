package de.hits.jobinstance.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.hits.jobinstance.common.SimpleManagingCache;
import de.hits.jobinstance.common.data.JobInstanceJob;
import de.hits.jobinstance.common.data.JobInstanceStatus;
import de.hits.jobinstance.common.utils.NumberUtils;
import de.hits.jobinstance.common.utils.RestPreconditions;
import de.hits.jobinstance.common.utils.StringUtils;
import de.hits.jobinstance.configuration.properties.ManageProperties;
import de.hits.jobinstance.domain.CatalogEntity;
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
public class JobInstanceJobController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ManageProperties manageProperties;

	private final AtomicLong jobInstanceIdSequence = new AtomicLong();

	private final AtomicLong counterCreated = new AtomicLong(0);
	private final AtomicLong counterSaved = new AtomicLong(0);
	private ConcurrentHashMap<String, AtomicLong> userCounter = new ConcurrentHashMap<>();

	private Map<Integer, CatalogEntity> catalogCache;
	private SimpleManagingCache<Long, JobInstanceJob> workCache;
	private ConcurrentHashMap<Long, JobInstanceJob> writeCache;

	private boolean useWriteCache = false;
	private boolean saveNonCached = false;
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

		this.saveNonCached = manageProperties.getSaveNonCachedJobs();

		this.catalogCache = new HashMap<>();

		long timeToLive = manageProperties.getCacheManagingMaxLifetime();
		long managingTimerInterval = manageProperties.getCacheManagingTimeInterval();
		this.workCache = new SimpleManagingCache<>(timeToLive, managingTimerInterval);

		boolean useWorkCacheLogging = manageProperties.isCacheLoggingEnabled();
		this.workCache.setLogging(true);

		boolean useWorkCacheMonitoring = manageProperties.isCacheMonitoringEnabled();
		long workCacheMonitoringInterval = manageProperties.getCacheMonitoringTimeInterval();
		this.workCache.setMonitoring(useWorkCacheMonitoring, workCacheMonitoringInterval);

		boolean useWriteCache = manageProperties.isWriteCacheEnabled();
		if (useWriteCache) {
			this.useWriteCache = true;
			this.writeCache = new ConcurrentHashMap<>(100000);
			this.writeTimeInterval = manageProperties.getWriteCacheTimeInterval();
			runPersistWriteCache();
		}

		this.jobInstanceIdSequence.set(jobService.getMaxJobId());

		this.log.info(String.format("Work cache: Maximum lifetime of an object in seconds: %s.",
				timeToLive));
		this.log.info(String.format("Work cache: Cleansing interval in seconds: %s.",
				managingTimerInterval));
		this.log.info(String.format("Work cache: Save non-cached enabled: %s.",
				this.saveNonCached));
		this.log.info(String.format("Work cache: Logging is enabled: %s.",
				useWorkCacheLogging));
		this.log.info(String.format("Work cache: Monitoring is enabled: %s.",
				useWorkCacheMonitoring));
		this.log.info(String.format("Work cache: Monitoring every %s seconds.",
				workCacheMonitoringInterval));

		this.log.info("Write cache: Write cache is enabled: " + this.useWriteCache);
		if (this.useWriteCache) {
			this.log.info(String.format("Write cache: Persist write cache interval in seconds: %s.",
					this.writeTimeInterval));
		}
		this.log.info("Maximum JobInstance-ID in the database: " + this.jobInstanceIdSequence.get());

		this.catalogService.listAllCatalogs()
				.forEach(catalog -> this.catalogCache.put(catalog.getCatalogId(), catalog));
	}

	@PreDestroy
	public void shutdownController() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#shutdownController()");
		}

		this.log.info("Shutdown requested, cleaning up and log statistics before shutdown.");
		this.log.info(String.format("... Jobs created: %s, jobs persisted: %s.", counterCreated, counterSaved));
		this.log.info("... Requests by user:");
		for (Map.Entry<String, AtomicLong> user : this.userCounter.entrySet()) {
			this.log.info(String.format("... User: %s, requests: %s.", user.getKey(), user.getValue().intValue()));
		}

		this.workCache.setLogging(false);
		this.workCache.setMonitoring(false, -1);
		this.log.info("... Work cache statistics:");
		this.workCache.monitor();

		this.log.info("... Persist write cache ...");
		persistWriteCache();

		this.workCache = null;
	}

	@RequestMapping(value = "/cache", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<JobInstanceJob> showCache() throws IOException {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#showCache()");
		}

		return this.workCache.getEntries().entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
	}

	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public JobInstanceJob findOne(@PathVariable("id") long id) throws IOException {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#findOne()");
		}

		JobInstanceJob json = null;

		JobInstanceJob processedJob = this.workCache.get(id);
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
	public JobInstanceJob create(@RequestBody JobInstanceStatus resource) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#create()");
		}

		this.counterCreated.incrementAndGet();
		String userName = addRequestForUserCounter();

		JobInstanceJob response = null;
		long jobInstanceId = this.jobInstanceIdSequence.incrementAndGet();

		boolean isCommonValid = !StringUtils.isEmpty(resource.getProjectName())
				&& !StringUtils.isEmpty(resource.getJobName());
		boolean isTechnicalValid = !StringUtils.isEmpty(resource.getHostName())
				&& !StringUtils.isEmpty(resource.getHostUser()) && !NumberUtils.isEmpty(resource.getHostPid());

		if (isCommonValid && isTechnicalValid) {
			if (resource.getJobStarted() == null) {
				resource.setJobStarted(LocalDateTime.now());
			}

			response = this.jobService.createJobInstance(jobInstanceId, resource);
			response.setAuthentication(userName);

			this.workCache.put(jobInstanceId, response);
		}

		return response;
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public JobInstanceJob save(@PathVariable("id") long id, @RequestBody JobInstanceJob resource) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#save()");
		}

		this.counterSaved.getAndIncrement();
		String userName = addRequestForUserCounter();
		String givenUserName = resource.getAuthentication();
		if (!StringUtils.equals(userName, givenUserName)) {
			log.info("Mismatch between the user name of the given request object and the authentication. ");
		}
		resource.setAuthentication(userName);

		JobInstanceStatus currentJob = resource.getCurrentJob();
		long jobInstanceId = currentJob.getJobInstanceId();
		if (currentJob.getJobEnded() == null) {
			currentJob.setJobEnded(LocalDateTime.now());
		}

		if (this.saveNonCached) {
			if (this.useWriteCache) {
				this.writeCache.put(jobInstanceId, resource);
			} else {
				this.jobService.persistJobInstance(resource);
			}

			// Verarbeiteten Job aus dem Cache entfernen, falls vorhanden.
			if (this.workCache.containsKey(jobInstanceId)) {
				this.workCache.remove(jobInstanceId);
			}
		} else {
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
		}

		return resource;
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseEntity<String> cancel(@PathVariable("id") long id) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#cancel()");
		}

		boolean wasInWorkCache = false;
		boolean wasInWriteCache = false;

		if (this.workCache.containsKey(id)) {
			wasInWorkCache = true;
			this.workCache.remove(id);
		}
		if (this.useWriteCache && this.writeCache.containsKey(id)) {
			wasInWriteCache = true;
			this.writeCache.remove(id);
		}

		StringBuilder responseMsg = new StringBuilder();
		if (!wasInWorkCache && !wasInWriteCache) {
			responseMsg.append(String.format("The job with the id %s was not cached.", id));
		} else {
			if (wasInWorkCache && !wasInWriteCache) {
				responseMsg.append(String.format("The job with the id %s was removed from the work cache.", id));
			} else if (!wasInWorkCache && wasInWriteCache) {
				responseMsg.append(String.format("The job with the id %s was removed from the write cache.", id));
			} else {
				responseMsg
						.append(String.format("The job with the id %s was removed from the work and write cache.", id));
			}
		}

		String msg = responseMsg.toString();
		ResponseEntity<String> response = new ResponseEntity<String>(msg, HttpStatus.OK);

		log.info(msg + " (Removing by an external call.)");

		return response;
	}

	@RequestMapping(value = "/previous", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public JobInstanceStatus previous(@RequestBody JobInstanceJob resource,
			@RequestParam(value = "successful", required = false) final Boolean successful,
			@RequestParam(value = "withInput", required = false) final Boolean withInput,
			@RequestParam(value = "withOutput", required = false) final Boolean withOutput,
			@RequestParam(value = "forWorkItem", required = false) final Boolean forWorkItem) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#previous()");
		}

		long jobInstanceId = resource.getCurrentJob().getJobInstanceId();
		JobInstanceJob cachedJob = this.workCache.get(jobInstanceId);

		// Wenn der Job im Cache existiert, soll diese Objekt verwendet und aktualisiert
		// werden.
		JobInstanceStatus previousJob = null;
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

	private String addRequestForUserCounter() {
		String userName = null;

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();

		if (authentication != null) {
			userName = authentication.getName();

			AtomicLong counter = this.userCounter.get(userName);
			if (counter == null) {
				counter = new AtomicLong();

				this.userCounter.put(userName, counter);
			}

			counter.incrementAndGet();
		}

		return userName;
	}
}