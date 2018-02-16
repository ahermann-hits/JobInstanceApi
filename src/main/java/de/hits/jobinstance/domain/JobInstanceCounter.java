package de.hits.jobinstance.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@Entity
@Table(name = "job_instance_counter", schema = "manage")
public class JobInstanceCounter {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOB_COUNTER_ID_SEQ")
	@SequenceGenerator(name = "JOB_COUNTER_ID_SEQ", sequenceName = "manage.JOB_COUNTER_ID_SEQ", initialValue = 1)
	@Column(name = "JOB_COUNTER_ID")
	private long jobCounterId;

	@Column(name = "JOB_INSTANCE_ID", nullable = false, insertable = true, updatable = false)
	private long jobInstanceId;

	@Column(name = "COUNTER_TYPE", nullable = false, insertable = true, updatable = false)
	private int counterType;
	@Column(name = "COUNTER_NAME", nullable = false, insertable = true, updatable = false, length = 128)
	private String counterName;
	@Column(name = "COUNTER_VALUE", nullable = false, insertable = true, updatable = false)
	private long counterValue;

	public JobInstanceCounter() {}

	/**
	 * @return the jobCounterId
	 */
	public long getJobCounterId() {
		return jobCounterId;
	}

	/**
	 * @param jobCounterId
	 *            the jobCounterId to set
	 */
	public void setJobCounterId(long jobCounterId) {
		this.jobCounterId = jobCounterId;
	}

	/**
	 * @return the jobInstanceId
	 */
	public long getJobInstanceId() {
		return jobInstanceId;
	}

	/**
	 * @param jobInstanceId
	 *            the jobInstanceId to set
	 */
	public void setJobInstanceId(long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}

	/**
	 * @return the counterType
	 */
	public int getCounterType() {
		return counterType;
	}

	/**
	 * @param counterType
	 *            the counterType to set
	 */
	public void setCounterType(int counterType) {
		this.counterType = counterType;
	}

	/**
	 * @return the counterName
	 */
	public String getCounterName() {
		return counterName;
	}

	/**
	 * @param counterName
	 *            the counterName to set
	 */
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	/**
	 * @return the counterValue
	 */
	public long getCounterValue() {
		return counterValue;
	}

	/**
	 * @param counterValue
	 *            the counterValue to set
	 */
	public void setCounterValue(long counterValue) {
		this.counterValue = counterValue;
	}

	@Override
	public String toString() {
		return String.format("Counter[id='%s', jiid='%s', type='%s', name='%s', value='%s']", getJobCounterId(),
				getJobInstanceId(), getCounterType(), getCounterName(), getCounterValue());
	}
}