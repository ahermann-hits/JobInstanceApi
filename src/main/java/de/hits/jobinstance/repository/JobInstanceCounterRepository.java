package de.hits.jobinstance.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hits.jobinstance.domain.JobInstanceCounterEntity;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public interface JobInstanceCounterRepository extends CrudRepository<JobInstanceCounterEntity, Long> {

	/**
	 * 
	 * @return
	 */
	List<JobInstanceCounterEntity> findAllByOrderByJobInstanceIdAscJobCounterIdAsc();

	/**
	 * 
	 * @param jiid
	 * @return
	 */
	List<JobInstanceCounterEntity> findByJobInstanceId(long jiid);
}