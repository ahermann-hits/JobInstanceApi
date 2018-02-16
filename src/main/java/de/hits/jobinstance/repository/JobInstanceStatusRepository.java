package de.hits.jobinstance.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hits.jobinstance.domain.JobInstanceStatus;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public interface JobInstanceStatusRepository extends CrudRepository<JobInstanceStatus, Long> {

	/**
	 * 
	 * @return
	 */
	List<JobInstanceStatus> findAllByOrderByJobInstanceId();

	/**
	 * 
	 * @param id
	 * @return
	 */
	List<JobInstanceStatus> findByProcessInstanceIdOrderByJobInstanceId(long id);
}