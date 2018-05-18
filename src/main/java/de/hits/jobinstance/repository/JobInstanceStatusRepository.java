package de.hits.jobinstance.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hits.jobinstance.domain.JobInstanceStatusEntity;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public interface JobInstanceStatusRepository extends CrudRepository<JobInstanceStatusEntity, Long> {

	/**
	 * 
	 * @return
	 */
	List<JobInstanceStatusEntity> findAllByOrderByJobInstanceId();

	/**
	 * 
	 * @param id
	 * @return
	 */
	List<JobInstanceStatusEntity> findByProcessInstanceIdOrderByJobInstanceId(long id);
}