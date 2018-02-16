package de.hits.jobinstance.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
@JsonPropertyOrder({ "CurrentJob", "Counter", "PreviousJob" })
public class JobInstanceJobJson {

	@JsonProperty("CurrentJob")
	private JobInstanceStatusJson currentJob;
	@JsonProperty("Counter")
	@Valid
	private List<JobInstanceCounterJson> counters = null;
	@JsonProperty("PreviousJob")
	@Valid
	private JobInstanceStatusJson previousJob = null;

	@JsonIgnore
	@Valid
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public JobInstanceJobJson() {
	}

	/**
	 * 
	 * @param job
	 */
	public JobInstanceJobJson(JobInstanceStatusJson job) {
		this.currentJob = job;
	}

	/**
	 * 
	 * @param job
	 * @param counters
	 */
	public JobInstanceJobJson(JobInstanceStatusJson job, List<JobInstanceCounterJson> counters) {
		this.currentJob = job;
		this.counters = counters;
	}

	/**
	 * @return the currentJob
	 */
	@JsonProperty("CurrentJob")
	public JobInstanceStatusJson getCurrentJob() {
		return currentJob;
	}

	/**
	 * @param currentJob
	 *            the currentJob to set
	 */
	@JsonProperty("CurrentJob")
	public void setCurrentJob(JobInstanceStatusJson currentJob) {
		this.currentJob = currentJob;
	}

	/**
	 * @return the counters
	 */
	@JsonProperty("Counter")
	public List<JobInstanceCounterJson> getCounters() {
		return counters;
	}

	/**
	 * @param counters
	 *            the counters to set
	 */
	@JsonProperty("Counter")
	public void setCounters(List<JobInstanceCounterJson> counters) {
		this.counters = counters;
	}

	/**
	 * 
	 * @param counter
	 */
	@JsonProperty("PreviousJobs")
	public void addCounter(JobInstanceCounterJson counter) {
		if (this.counters == null) {
			this.counters = new ArrayList<>();
		}
		this.counters.add(counter);
	}

	/**
	 * @return the previousJob
	 */
	@JsonProperty("PreviousJob")
	public JobInstanceStatusJson getPreviousJob() {
		return previousJob;
	}

	/**
	 * @param previousJob
	 *            the previousJob to set
	 */
	@JsonProperty("PreviousJobs")
	public void setPreviousJobs(JobInstanceStatusJson previousJob) {
		this.previousJob = previousJob;
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
		return String.format("JobInstance[currentJob=%s, counters='%s', previousJobs='%s']", getCurrentJob(),
				getCounters(), getPreviousJob());
	}
}