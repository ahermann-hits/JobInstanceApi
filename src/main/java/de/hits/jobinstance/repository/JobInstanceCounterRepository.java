package de.hits.jobinstance.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.hits.jobinstance.domain.JobInstanceCounter;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public interface JobInstanceCounterRepository extends CrudRepository<JobInstanceCounter, Long> {

	/**
	 * 
	 * @return
	 */
	List<JobInstanceCounter> findAllByOrderByJobInstanceIdAscJobCounterIdAsc();

	/**
	 * 
	 * @param jiid
	 * @return
	 */
	List<JobInstanceCounter> findByJobInstanceId(long jiid);
}