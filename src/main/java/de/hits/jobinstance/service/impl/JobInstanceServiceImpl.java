package de.hits.jobinstance.service.impl;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import de.hits.jobinstance.common.data.JobInstanceCounter;
import de.hits.jobinstance.common.data.JobInstanceJob;
import de.hits.jobinstance.common.data.JobInstanceStatus;
import de.hits.jobinstance.data.CatalogJson;
import de.hits.jobinstance.domain.CatalogEntity;
import de.hits.jobinstance.domain.JobInstanceCounterEntity;
import de.hits.jobinstance.domain.JobInstanceStatusEntity;
import de.hits.jobinstance.repository.JobInstanceCounterRepository;
import de.hits.jobinstance.repository.JobInstanceStatusRepository;
import de.hits.jobinstance.service.JobInstanceService;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@Service
public class JobInstanceServiceImpl implements JobInstanceService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private Map<Integer, CatalogEntity> counterCatalogs;

	private JobInstanceStatusRepository jobInstanceRepository;
	private JobInstanceCounterRepository jobCounterRepository;

	@Autowired
	private CatalogServiceImpl catalogService;

	@Autowired
	protected Environment env;
	@Autowired
	EntityManager em;

	public static enum CounterType {
		FETCH("counter_fetch"),
		INSERT("counter_insert"),
		UPDATE("counter_update"),
		REJECT("counter_reject"),
		DELETE("counter_delete");

		private String sysname;

		private CounterType(String sysname) {
			this.sysname = sysname;
		}

		public String getSysname() {
			return sysname;
		}

		public static CounterType valueBySysname(String sysname) throws NotFoundException {
			List<CounterType> list = Arrays.asList(CounterType.values());
			return list.stream().filter(i -> i.getSysname().equals(sysname)).findFirst()
					.orElseThrow(NotFoundException::new);
		}
	}

	@PostConstruct
	private void init() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#init()");
		}

		List<String> sysnames = Arrays.asList(CounterType.values()).stream()
				.map(counterType -> counterType.getSysname()).collect(Collectors.toList());

		List<CatalogEntity> catalogs = sysnames.stream()
				.map(sysname -> CompletableFuture.supplyAsync(() -> this.catalogService.getCatalogBySysname(sysname)))
				.map(CompletableFuture::join).collect(Collectors.toList());

		this.counterCatalogs = new HashMap<>();
		catalogs.forEach(catalog -> this.counterCatalogs.put(catalog.getCatalogId(), catalog));
	}

	@Autowired
	public void setRepositories(JobInstanceStatusRepository jobInstanceRepository,
			JobInstanceCounterRepository jobCounterRepository) {
		this.jobInstanceRepository = jobInstanceRepository;
		this.jobCounterRepository = jobCounterRepository;
	}

	@Override
	public List<JobInstanceStatusEntity> listAllJobs() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#listAllJobs()");
		}

		return this.jobInstanceRepository.findAllByOrderByJobInstanceId();
	}

	@Override
	public List<JobInstanceStatusEntity> listAllJobsForProcessId(long id) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#listAllForProcessId()");
		}

		return this.jobInstanceRepository.findByProcessInstanceIdOrderByJobInstanceId(id);
	}

	@Override
	public long getMaxJobId() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#getJobById()");
		}

		String qlString = "select coalesce(max(j.job_instance_id), 0) from manage.job_instance_status j";
		BigInteger singleResult = (BigInteger) em.createNativeQuery(qlString).getSingleResult();

		return singleResult.longValue();
	}

	@Override
	public JobInstanceStatusEntity getJobById(long id) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#getJobById()");
		}

		return this.jobInstanceRepository.findOne(id);
	}

	@Override
	public JobInstanceStatusEntity retrievePreviousJobInstance(long jobInstanceId, String jobName, String workItem,
			Boolean successful, Boolean withInput, Boolean withOutput, Boolean forWorkItem) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#retrievePreviousJobInstance()");
		}

		String successfulStr = "";
		if (successful != null) {
			successfulStr = successful ? "AND j.return_code = 0 " : "AND j.return_code <> 0 ";
		}
		String withInputStr = "";
		if (withInput != null) {
			withInputStr = withInput ? "AND j.count_input > 0 " : "AND j.count_input = 0 ";
		}
		String withOutputStr = "";
		if (withOutput != null) {
			withOutputStr = withOutput ? "AND (j.count_insert > 0 OR j.count_update > 0) "
					: "AND j.count_insert = 0 AND j.count_update = 0 ";
		}
		String forWorkItemStr = "";
		if (workItem != null) {
			if (forWorkItem != null) {
				forWorkItemStr = forWorkItem ? "AND j.work_item = '" + workItem + "' "
						: "AND j.work_item <> '" + workItem + "' ";
			}
		} else {
			if (forWorkItem != null) {
				forWorkItemStr = forWorkItem ? "AND j.work_item IS NULL " : "AND j.work_item IS NOT NULL ";
			}
		}

		StringBuilder query = new StringBuilder();
		query.append("SELECT j.* ");
		query.append("FROM manage.job_instance_status j ");
		query.append("WHERE j.job_instance_id < %s ");
		query.append("AND j.job_name = '%s' ");
		query.append(successfulStr);
		query.append(withInputStr);
		query.append(withOutputStr);
		query.append(forWorkItemStr);
		query.append("ORDER BY j.job_instance_id DESC ");
		query.append("LIMIT 1");
		String qlString = String.format(query.toString(), jobInstanceId, jobName);

		Query queryResult = this.em.createNativeQuery(qlString, JobInstanceStatusEntity.class).setFirstResult(0);
		@SuppressWarnings("unchecked")
		List<JobInstanceStatusEntity> resultList = queryResult.getResultList();
		if (resultList != null && resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}

	@Override
	public JobInstanceStatusEntity saveJob(JobInstanceStatusEntity job) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#saveJob()");
		}

		JobInstanceStatusEntity savedJob = this.jobInstanceRepository.save(job);
		if (this.log.isDebugEnabled()) {
			this.log.debug("JobInstanceStatus angelegt: " + savedJob.toString());
		}

		return savedJob;
	}

	@Override
	public List<JobInstanceStatusEntity> saveAllJobs(List<JobInstanceStatusEntity> jobs) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#saveAllJobs()");
		}

		List<JobInstanceStatusEntity> savedJobs = new ArrayList<>();
		Iterable<JobInstanceStatusEntity> iterable = this.jobInstanceRepository.save(jobs);
		iterable.forEach(savedJobs::add);
		if (this.log.isDebugEnabled()) {
			this.log.debug(savedJobs.size() + " JobInstanceStatus angelegt.");
		}

		return savedJobs;
	}

	@Override
	public JobInstanceStatus convertJobToJson(JobInstanceStatusEntity job) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#convertJobToJson()");
		}

		JobInstanceStatus response = null;

		if (job != null) {
			response = new JobInstanceStatus(job.getJobInstanceId(), job.getProcessInstanceId(), job.getProjectName(),
					job.getJobName(), job.getJobGUID(), job.getWorkItem(), job.getTimeRangeStart(),
					job.getTimeRangeEnd(), job.getValueRangeStart(), job.getValueRangeEnd(), job.getCountInput(),
					job.getCountInsert(), job.getCountUpdate(), job.getCountReject(), job.getCountDelete(),
					job.getJobStarted(), job.getJobEnded(), job.getJobResult(), job.getReturnCode(),
					job.getReturnMessage(), job.getHostName(), job.getHostPid(), job.getHostUser(),
					job.getServiceUser());
		}

		return response;
	}

	@Override
	public JobInstanceStatusEntity convertJsonToJob(JobInstanceStatus json) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#convertJobToJson()");
		}

		JobInstanceStatusEntity job = null;

		if (json != null) {
			job = new JobInstanceStatusEntity();
			job.setJobInstanceId(json.getJobInstanceId());
			job.setProcessInstanceId(json.getProcessInstanceId());
			job.setProjectName(json.getProjectName());
			job.setJobName(json.getJobName());
			job.setJobGUID(json.getJobGUID());
			job.setWorkItem(json.getWorkItem());
			job.setTimeRangeStart(json.getTimeRangeStart());
			job.setTimeRangeEnd(json.getTimeRangeEnd());
			job.setValueRangeStart(json.getValueRangeStart());
			job.setValueRangeEnd(json.getValueRangeEnd());
			job.setCountInput(json.getCountInput());
			job.setCountInsert(json.getCountInsert());
			job.setCountUpdate(json.getCountUpdate());
			job.setCountReject(json.getCountReject());
			job.setCountDelete(json.getCountDelete());
			job.setJobStarted(json.getJobStarted());
			job.setJobEnded(json.getJobEnded());
			job.setJobResult(json.getJobResult());
			job.setReturnCode(json.getReturnCode());
			job.setReturnMessage(json.getReturnMessage());
			job.setHostName(json.getHostName());
			job.setHostPid(json.getHostPid());
			job.setHostUser(json.getHostUser());
			job.setServiceUser(json.getServiceUser());
		}

		return job;
	}

	@Override
	public List<JobInstanceCounterEntity> listAllCounter() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#listAllCounter()");
		}

		return this.jobCounterRepository.findAllByOrderByJobInstanceIdAscJobCounterIdAsc();
	}

	@Override
	public List<JobInstanceCounterEntity> listAllCounterForJob(long id) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#listAllCounterForJob()");
		}

		return this.jobCounterRepository.findByJobInstanceId(id);
	}

	@Override
	public JobInstanceCounterEntity getCounterById(long id) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#getCounterById()");
		}

		return this.jobCounterRepository.findOne(id);
	}

	@Override
	public JobInstanceCounterEntity saveCounter(JobInstanceCounterEntity counter) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#saveCounter()");
		}

		JobInstanceCounterEntity savedCounter = this.jobCounterRepository.save(counter);
		if (this.log.isDebugEnabled()) {
			this.log.debug("JobInstanceCounter angelegt: " + savedCounter.toString());
		}

		return savedCounter;
	}

	@Override
	public List<JobInstanceCounterEntity> saveAllCounters(List<JobInstanceCounterEntity> counters) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#saveAllCounters()");
		}

		List<JobInstanceCounterEntity> savedCounters = new ArrayList<>();
		Iterable<JobInstanceCounterEntity> iterable = this.jobCounterRepository.save(counters);
		iterable.forEach(savedCounters::add);
		if (this.log.isDebugEnabled()) {
			this.log.debug(savedCounters.size() + " JobInstanceCounter angelegt.");
		}

		return savedCounters;
	}

	@Override
	public JobInstanceCounter convertCounterToJson(JobInstanceCounterEntity counter) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#convertCounterToJson()");
		}

		JobInstanceCounter response = null;

		if (counter != null) {
			response = new JobInstanceCounter(counter.getJobCounterId(), counter.getJobInstanceId(),
					counter.getCounterType(), counter.getCounterName(), counter.getCounterValue());
		}

		return response;
	}

	@Override
	public JobInstanceCounterEntity convertJsonToCounter(JobInstanceCounter json) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#convertCounterToJson()");
		}

		JobInstanceCounterEntity counter = null;

		if (json != null) {
			Long jobCounterId = json.getJobCounterId();

			counter = new JobInstanceCounterEntity();
			if (jobCounterId != null) {
				counter.setJobCounterId(jobCounterId);
			}
			counter.setJobInstanceId(json.getJobInstanceId());
			counter.setCounterType(json.getCounterType());
			counter.setCounterName(json.getCounterName());
			counter.setCounterValue(json.getCounterValue());
		}

		return counter;
	}

	/**
	 * 
	 * @param jobName
	 * @param workItem
	 * @return
	 * @throws Exception
	 */
	@Override
	public JobInstanceJob createJobInstance(long jobInstanceId, JobInstanceStatus resource) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#createRootJobRun()");
		}

		resource.setJobInstanceId(jobInstanceId);
		resource.setJobGUID(generateProcessUUID());
		if (resource.getJobStarted() == null) {
			resource.setJobStarted(LocalDateTime.now());
		}

		Long processInstanceId = resource.getProcessInstanceId();
		boolean isCalledJob = processInstanceId != null && processInstanceId >= 0;

		JobInstanceJob json = new JobInstanceJob(resource);

		if (log.isDebugEnabled()) {
			if (isCalledJob) {
				this.log.debug(String.format("Job created: %s", json.toString()));
			} else {
				this.log.debug(String.format("Process created: %s", json.toString()));
			}
		}

		return json;
	}

	@Override
	public void retrievePreviousInstanceData(JobInstanceJob job, Boolean successful, Boolean withInput,
			Boolean withOutput, Boolean forWorkItem) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#retrievePreviousInstanceData()");
		}

		if (job.getPreviousJob() == null) {
			JobInstanceStatus currentJob = job.getCurrentJob();
			JobInstanceStatusEntity previousJob = retrievePreviousJobInstance(currentJob.getJobInstanceId(),
					currentJob.getJobName(), currentJob.getWorkItem(), successful, withInput, withOutput, forWorkItem);
			job.setPreviousJob(convertJobToJson(previousJob));
		}
	}

	@Override
	public void createCounter(JobInstanceJob job, String counterName, CatalogJson counterType,
			long counterValue) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#createCounter()");
		}

		JobInstanceCounter json = new JobInstanceCounter(null, job.getCurrentJob().getJobInstanceId(),
				counterType.getCatalogId(), counterName, counterValue);

		job.addCounter(json);
	}

	@Override
	public Long persistJobInstance(JobInstanceJob job) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#persistJobInstance()");
		}

		Map<Long, JobInstanceJob> jobToSave = new HashMap<>();
		jobToSave.put(job.getCurrentJob().getJobInstanceId(), job);

		return persistJobInstanceAll(jobToSave).get(0);
	}

	@Override
	public List<Long> persistJobInstanceAll(Map<Long, JobInstanceJob> jobs) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#persistJobInstanceAll()");
		}

		Map<Long, JobInstanceStatusEntity> jobsToSave = new HashMap<>();
		List<JobInstanceCounterEntity> counterToSave = new ArrayList<>();

		for (Map.Entry<Long, JobInstanceJob> job : jobs.entrySet()) {
			JobInstanceStatus jobInstanceJson = job.getValue().getCurrentJob();
			List<JobInstanceCounter> counterJson = job.getValue().getCounters();

			int countInput = 0;
			int countInsert = 0;
			int countUpdate = 0;
			int countReject = 0;
			int countDelete = 0;

			if (counterJson != null && !counterJson.isEmpty()) {
				for (JobInstanceCounter counter : counterJson) {
					CounterType counterType;
					try {
						CatalogEntity counterTypeCatalog = this.counterCatalogs.get(counter.getCounterType());

						counterType = CounterType.valueBySysname(counterTypeCatalog.getSysname());
						switch (counterType) {
						case FETCH:
							countInput += counter.getCounterValue();
							break;
						case INSERT:
							countInsert += counter.getCounterValue();
							break;
						case UPDATE:
							countUpdate += counter.getCounterValue();
							break;
						case REJECT:
							countReject += counter.getCounterValue();
							break;
						case DELETE:
							countDelete += counter.getCounterValue();
							break;
						}
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
				}

				if (counterJson.size() == 1) {
					counterToSave.add(convertJsonToCounter(counterJson.get(0)));
				} else {
					counterJson.forEach(counter -> counterToSave.add(convertJsonToCounter(counter)));
				}
			}

			jobInstanceJson.setCountInput(countInput);
			jobInstanceJson.setCountInsert(countInsert);
			jobInstanceJson.setCountUpdate(countUpdate);
			jobInstanceJson.setCountReject(countReject);
			jobInstanceJson.setCountDelete(countDelete);

			jobsToSave.put(jobInstanceJson.getJobInstanceId(), convertJsonToJob(jobInstanceJson));
		}

		if (!jobsToSave.isEmpty()) {
			saveAllJobs(jobsToSave.entrySet().stream()
					.map(e -> e.getValue()).collect(Collectors.toList()))
					.forEach(savedJob -> {
						JobInstanceJob job = jobs.get(savedJob.getJobInstanceId());
						job.setCurrentJob(convertJobToJson(savedJob));
					});
		}

		if (!counterToSave.isEmpty()) {
			// Counter in allen Json Jobs löschen
			jobs.entrySet().forEach(job -> job.getValue().setCounters(new ArrayList<>()));
			// Counter in allen Json Jobs neu befüllen
			saveAllCounters(counterToSave)
					.forEach(savedCounter -> {
						JobInstanceJob job = jobs.get(savedCounter.getJobInstanceId());
						job.addCounter(convertCounterToJson(savedCounter));
					});
		}

		return jobsToSave.keySet().stream().collect(Collectors.toList());
	}

	/**
	 * 
	 * @return
	 */
	private String generateProcessUUID() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#generateProcessUUID()");
		}

		UUID uuid = UUID.randomUUID();
		return "" + getProcessId() + uuid.toString();
	}

	/**
	 * 
	 * @return the process id from the jvm of this process.
	 */
	private long getProcessId() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#getProcessId()");
		}

		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		String jvmName = runtimeBean.getName();
		long pid = Long.valueOf(jvmName.split("@")[0]);

		return pid;
	}
}