package de.hits.jobinstance.service;

import java.util.List;
import java.util.Map;

import de.hits.jobinstance.common.data.JobInstanceCounter;
import de.hits.jobinstance.common.data.JobInstanceJob;
import de.hits.jobinstance.common.data.JobInstanceStatus;
import de.hits.jobinstance.data.CatalogJson;
import de.hits.jobinstance.domain.JobInstanceCounterEntity;
import de.hits.jobinstance.domain.JobInstanceStatusEntity;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public interface JobInstanceService {

	/**
	 * 
	 * @return
	 */
	List<JobInstanceStatusEntity> listAllJobs();

	/**
	 * 
	 * @param id
	 * @return
	 */
	List<JobInstanceStatusEntity> listAllJobsForProcessId(long id);

	/**
	 * 
	 * @return
	 */
	long getMaxJobId();

	/**
	 * 
	 * @param id
	 * @return
	 */
	JobInstanceStatusEntity getJobById(long id);

	/**
	 * 
	 * @param jobInstanceId
	 * @param jobName
	 * @param workItem
	 * @param successful
	 * @param withInput
	 * @param withOutput
	 * @param forWorkItem
	 * @return
	 */
	JobInstanceStatusEntity retrievePreviousJobInstance(long jobInstanceId, String jobName, String workItem,
			Boolean successful, Boolean withInput, Boolean withOutput, Boolean forWorkItem);

	/**
	 * Speichert den übergebenen {@link JobInstanceStatus Job} in der Datenbank.
	 * 
	 * @param job
	 *            Der Job der in der Datenbank gespeichert werden soll.
	 * @return Die Instanz des gespeicherten Datensatzes aus der Datenbank.
	 */
	JobInstanceStatusEntity saveJob(JobInstanceStatusEntity job);

	/**
	 * 
	 * @param jobs
	 * @return
	 */
	List<JobInstanceStatusEntity> saveAllJobs(List<JobInstanceStatusEntity> jobs);

	/**
	 * 
	 * @param job
	 * @return
	 */
	JobInstanceStatus convertJobToJson(JobInstanceStatusEntity job);

	/**
	 * 
	 * @param json
	 * @return
	 */
	JobInstanceStatusEntity convertJsonToJob(JobInstanceStatus json);

	/**
	 * 
	 * @return
	 */
	List<JobInstanceCounterEntity> listAllCounter();

	/**
	 * 
	 * @param id
	 * @return
	 */
	List<JobInstanceCounterEntity> listAllCounterForJob(long id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	JobInstanceCounterEntity getCounterById(long id);

	/**
	 * Speichert den übergebenen {@link JobInstanceCounter Zähler} in der Datenbank.
	 * 
	 * @param counter
	 *            Der Zähler der in der Datenbank gespeichert werden soll.
	 * @return Die Instanz des gespeicherten Datensatzes aus der Datenbank.
	 */
	JobInstanceCounterEntity saveCounter(JobInstanceCounterEntity counter);

	/**
	 * Speichert die übergebenen {@link JobInstanceCounter Zähler} in der Datenbank.
	 * 
	 * @param counters
	 *            Die Zähler die in der Datenbank gespeichert werden soll.
	 * @return Die Instanzen der gespeicherten Datensätze aus der Datenbank.
	 */
	List<JobInstanceCounterEntity> saveAllCounters(List<JobInstanceCounterEntity> counters);

	/**
	 * 
	 * @param counter
	 * @return
	 */
	JobInstanceCounter convertCounterToJson(JobInstanceCounterEntity counter);

	/**
	 * 
	 * @param json
	 * @return
	 */
	JobInstanceCounterEntity convertJsonToCounter(JobInstanceCounter json);

	/**
	 * 
	 * @param jobInstanceId
	 * @param resource
	 * @return
	 */
	JobInstanceJob createJobInstance(long jobInstanceId, JobInstanceStatus resource);

	/**
	 * 
	 * @param job
	 * @param successful
	 * @param withInput
	 * @param withOutput
	 * @param forWorkItem
	 */
	void retrievePreviousInstanceData(JobInstanceJob job, Boolean successful, Boolean withInput, Boolean withOutput,
			Boolean forWorkItem);

	/**
	 * 
	 * @param job
	 * @param counterName
	 * @param counterType
	 * @param counterValue
	 * @return
	 */
	void createCounter(JobInstanceJob job, String counterName, CatalogJson counterType, long counterValue);

	/**
	 * 
	 * @param job
	 * @return
	 */
	Long persistJobInstance(JobInstanceJob job);

	/**
	 * 
	 * @param jobs
	 * @return
	 */
	List<Long> persistJobInstanceAll(Map<Long, JobInstanceJob> jobs);

	/**
	 * 
	 * @return
	 */
	String generateProcessUUID();
}