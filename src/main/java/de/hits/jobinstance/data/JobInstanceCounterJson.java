package de.hits.jobinstance.data;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "JobCounterId", "JobInstanceId", "CounterType", "CounterName", "CounterValue" })
public class JobInstanceCounterJson {

	@JsonProperty("JobCounterId")
	private Long jobCounterId;
	@JsonProperty("JobInstanceId")
	private long jobInstanceId;
	@JsonProperty("CounterType")
	private int counterType;
	@JsonProperty("CounterName")
	private String counterName;
	@JsonProperty("CounterValue")
	private long counterValue;
	@JsonIgnore
	@Valid
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public JobInstanceCounterJson() {
	}

	/**
	 * 
	 * @param jobCounterId
	 * @param jobInstanceId
	 * @param counterType
	 * @param counterName
	 * @param counterValue
	 */
	public JobInstanceCounterJson(Long jobCounterId, long jobInstanceId, int counterType, String counterName,
			long counterValue) {
		super();
		this.jobCounterId = jobCounterId;
		this.jobInstanceId = jobInstanceId;
		this.counterType = counterType;
		this.counterName = counterName;
		this.counterValue = counterValue;
	}

	/**
	 * @return the jobCounterId
	 */
	@JsonProperty("JobCounterId")
	public Long getJobCounterId() {
		return jobCounterId;
	}

	/**
	 * @param jobCounterId
	 *            the jobCounterId to set
	 */
	@JsonProperty("JobCounterId")
	public void setJobCounterId(Long jobCounterId) {
		this.jobCounterId = jobCounterId;
	}

	/**
	 * @return the jobInstanceId
	 */
	@JsonProperty("JobInstanceId")
	public long getJobInstanceId() {
		return jobInstanceId;
	}

	/**
	 * @param jobInstanceId
	 *            the jobInstanceId to set
	 */
	@JsonProperty("JobInstanceId")
	public void setJobInstanceId(long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}

	/**
	 * @return the counterType
	 */
	@JsonProperty("CounterType")
	public int getCounterType() {
		return counterType;
	}

	/**
	 * @param counterType
	 *            the counterType to set
	 */
	@JsonProperty("CounterType")
	public void setCounterType(int counterType) {
		this.counterType = counterType;
	}

	/**
	 * @return the counterName
	 */
	@JsonProperty("CounterName")
	public String getCounterName() {
		return counterName;
	}

	/**
	 * @param counterName
	 *            the counterName to set
	 */
	@JsonProperty("CounterName")
	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	/**
	 * @return the counterValue
	 */
	@JsonProperty("CounterValue")
	public long getCounterValue() {
		return counterValue;
	}

	/**
	 * @param counterValue
	 *            the counterValue to set
	 */
	@JsonProperty("CounterValue")
	public void setCounterValue(long counterValue) {
		this.counterValue = counterValue;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return String.format("Counter[id='%s', jiid='%s', type='%s', name='%s', value='%s']", getJobCounterId(),
				getJobInstanceId(), getCounterType(), getCounterName(), getCounterValue());
	}
}