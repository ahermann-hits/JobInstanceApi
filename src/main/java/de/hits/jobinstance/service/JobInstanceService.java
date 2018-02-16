package de.hits.jobinstance.service;

import java.util.List;
import java.util.Map;

import de.hits.jobinstance.data.CatalogJson;
import de.hits.jobinstance.data.JobInstanceCounterJson;
import de.hits.jobinstance.data.JobInstanceJobJson;
import de.hits.jobinstance.data.JobInstanceStatusJson;
import de.hits.jobinstance.domain.JobInstanceCounter;
import de.hits.jobinstance.domain.JobInstanceStatus;

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
	List<JobInstanceStatus> listAllJobs();

	/**
	 * 
	 * @param id
	 * @return
	 */
	List<JobInstanceStatus> listAllJobsForProcessId(long id);

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
	JobInstanceStatus getJobById(long id);

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
	JobInstanceStatus retrievePreviousJobInstance(long jobInstanceId, String jobName, String workItem,
			Boolean successful, Boolean withInput, Boolean withOutput, Boolean forWorkItem);

	/**
	 * Speichert den übergebenen {@link JobInstanceStatus Job} in der Datenbank.
	 * 
	 * @param job
	 *            Der Job der in der Datenbank gespeichert werden soll.
	 * @return Die Instanz des gespeicherten Datensatzes aus der Datenbank.
	 */
	JobInstanceStatus saveJob(JobInstanceStatus job);

	/**
	 * 
	 * @param jobs
	 * @return
	 */
	List<JobInstanceStatus> saveAllJobs(List<JobInstanceStatus> jobs);

	/**
	 * 
	 * @param job
	 * @return
	 */
	JobInstanceStatusJson convertJobToJson(JobInstanceStatus job);

	/**
	 * 
	 * @param json
	 * @return
	 */
	JobInstanceStatus convertJsonToJob(JobInstanceStatusJson json);

	/**
	 * 
	 * @return
	 */
	List<JobInstanceCounter> listAllCounter();

	/**
	 * 
	 * @param id
	 * @return
	 */
	List<JobInstanceCounter> listAllCounterForJob(long id);

	/**
	 * 
	 * @param id
	 * @return
	 */
	JobInstanceCounter getCounterById(long id);

	/**
	 * Speichert den übergebenen {@link JobInstanceCounter Zähler} in der Datenbank.
	 * 
	 * @param counter
	 *            Der Zähler der in der Datenbank gespeichert werden soll.
	 * @return Die Instanz des gespeicherten Datensatzes aus der Datenbank.
	 */
	JobInstanceCounter saveCounter(JobInstanceCounter counter);

	/**
	 * Speichert die übergebenen {@link JobInstanceCounter Zähler} in der Datenbank.
	 * 
	 * @param counters
	 *            Die Zähler die in der Datenbank gespeichert werden soll.
	 * @return Die Instanzen der gespeicherten Datensätze aus der Datenbank.
	 */
	List<JobInstanceCounter> saveAllCounters(List<JobInstanceCounter> counters);

	/**
	 * 
	 * @param counter
	 * @return
	 */
	JobInstanceCounterJson convertCounterToJson(JobInstanceCounter counter);

	/**
	 * 
	 * @param json
	 * @return
	 */
	JobInstanceCounter convertJsonToCounter(JobInstanceCounterJson json);

	/**
	 * 
	 * @param jobInstanceId
	 * @param resource
	 * @return
	 */
	JobInstanceJobJson createJobInstance(long jobInstanceId, JobInstanceStatusJson resource);

	/**
	 * 
	 * @param job
	 * @param successful
	 * @param withInput
	 * @param withOutput
	 * @param forWorkItem
	 */
	void retrievePreviousInstanceData(JobInstanceJobJson job, Boolean successful, Boolean withInput, Boolean withOutput,
			Boolean forWorkItem);

	/**
	 * 
	 * @param job
	 * @param counterName
	 * @param counterType
	 * @param counterValue
	 * @return
	 */
	void createCounter(JobInstanceJobJson job, String counterName, CatalogJson counterType, long counterValue);

	/**
	 * 
	 * @param job
	 */
	void persistJobInstance(JobInstanceJobJson job);

	/**
	 * 
	 * @param jobs
	 * @return
	 */
	List<Long> persistJobInstanceAll(Map<Long, JobInstanceJobJson> jobs);

	/**
	 * 
	 * @return
	 */
	String generateProcessUUID();
}