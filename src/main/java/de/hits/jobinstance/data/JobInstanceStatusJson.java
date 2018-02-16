package de.hits.jobinstance.data;

import java.time.LocalDateTime;
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
@JsonPropertyOrder({ "JobInstanceId", "ProcessInstanceId", "ProjectName", "JobName", "JobGUID", "WorkItem",
		"TimeRangeStart", "TimeRangeEnd", "ValueRangeStart", "ValueRangeEnd", "CountInput", "CountInsert",
		"CountUpdate", "CountReject", "CountDelete", "JobStarted", "JobEnded", "JobResult", "ReturnCode",
		"ReturnMessage", "HostName", "HostPID", "HostUser" })
public class JobInstanceStatusJson {

	@JsonProperty("JobInstanceId")
	private long jobInstanceId;
	@JsonProperty("ProcessInstanceId")
	private Long processInstanceId;

	@JsonProperty("ProjectName")
	private String projectName;
	@JsonProperty("JobName")
	private String jobName;
	@JsonProperty("JobGUID")
	private String jobGUID;

	@JsonProperty("WorkItem")
	private String workItem;

	@JsonProperty("TimeRangeStart")
	private LocalDateTime timeRangeStart;
	@JsonProperty("TimeRangeEnd")
	private LocalDateTime timeRangeEnd;
	@JsonProperty("ValueRangeStart")
	private Long valueRangeStart;
	@JsonProperty("ValueRangeEnd")
	private Long valueRangeEnd;

	@JsonProperty("CountInput")
	private Integer countInput;
	@JsonProperty("CountInsert")
	private Integer countInsert;
	@JsonProperty("CountUpdate")
	private Integer countUpdate;
	@JsonProperty("CountReject")
	private Integer countReject;
	@JsonProperty("CountDelete")
	private Integer countDelete;

	@JsonProperty("JobStarted")
	private LocalDateTime jobStarted;
	@JsonProperty("JobEnded")
	private LocalDateTime jobEnded;
	@JsonProperty("JobResult")
	private String jobResult;

	@JsonProperty("ReturnCode")
	private Integer returnCode;
	@JsonProperty("ReturnMessage")
	private String returnMessage;

	@JsonProperty("HostName")
	private String hostName;
	@JsonProperty("HostPID")
	private Integer hostPid;
	@JsonProperty("HostUser")
	private String hostUser;

	@JsonIgnore
	@Valid
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * No args constructor for use in serialization
	 * 
	 */
	public JobInstanceStatusJson() {
	}

	/**
	 * 
	 * @param jobInstanceId
	 * @param processInstanceId
	 * @param projectName
	 * @param jobName
	 * @param jobGUID
	 * @param workItem
	 * @param timeRangeStart
	 * @param timeRangeEnd
	 * @param valueRangeStart
	 * @param valueRangeEnd
	 * @param countInput
	 * @param countInsert
	 * @param countUpdate
	 * @param countReject
	 * @param countDelete
	 * @param jobStarted
	 * @param jobEnded
	 * @param jobResult
	 * @param returnCode
	 * @param returnMessage
	 * @param hostName
	 * @param hostPid
	 * @param hostUser
	 */
	public JobInstanceStatusJson(long jobInstanceId, Long processInstanceId, String projectName, String jobName,
			String jobGUID, String workItem, LocalDateTime timeRangeStart, LocalDateTime timeRangeEnd,
			Long valueRangeStart, Long valueRangeEnd, Integer countInput, Integer countInsert, Integer countUpdate,
			Integer countReject, Integer countDelete, LocalDateTime jobStarted, LocalDateTime jobEnded,
			String jobResult, Integer returnCode, String returnMessage, String hostName, Integer hostPid,
			String hostUser) {
		super();
		this.jobInstanceId = jobInstanceId;
		this.processInstanceId = processInstanceId;
		this.projectName = projectName;
		this.jobName = jobName;
		this.jobGUID = jobGUID;
		this.workItem = workItem;
		this.timeRangeStart = timeRangeStart;
		this.timeRangeEnd = timeRangeEnd;
		this.valueRangeStart = valueRangeStart;
		this.valueRangeEnd = valueRangeEnd;
		this.countInput = countInput;
		this.countInsert = countInsert;
		this.countUpdate = countUpdate;
		this.countReject = countReject;
		this.countDelete = countDelete;
		this.jobStarted = jobStarted;
		this.jobEnded = jobEnded;
		this.jobResult = jobResult;
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
		this.hostName = hostName;
		this.hostPid = hostPid;
		this.hostUser = hostUser;
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
	 * @return the processInstanceId
	 */
	@JsonProperty("ProcessInstanceId")
	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	/**
	 * @param processInstanceId
	 *            the processInstanceId to set
	 */
	@JsonProperty("ProcessInstanceId")
	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	/**
	 * @return the projectName
	 */
	@JsonProperty("ProjectName")
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	@JsonProperty("ProjectName")
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the jobName
	 */
	@JsonProperty("JobName")
	public String getJobName() {
		return jobName;
	}

	/**
	 * @param jobName
	 *            the jobName to set
	 */
	@JsonProperty("JobName")
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * @return the jobGUID
	 */
	@JsonProperty("JobGUID")
	public String getJobGUID() {
		return jobGUID;
	}

	/**
	 * @param jobGUID
	 *            the jobGUID to set
	 */
	@JsonProperty("JobGUID")
	public void setJobGUID(String jobGUID) {
		this.jobGUID = jobGUID;
	}

	/**
	 * @return the workItem
	 */
	@JsonProperty("WorkItem")
	public String getWorkItem() {
		return workItem;
	}

	/**
	 * @param workItem
	 *            the workItem to set
	 */
	@JsonProperty("WorkItem")
	public void setWorkItem(String workItem) {
		this.workItem = workItem;
	}

	/**
	 * @return the timeRangeStart
	 */
	@JsonProperty("TimeRangeStart")
	public LocalDateTime getTimeRangeStart() {
		return timeRangeStart;
	}

	/**
	 * @param timeRangeStart
	 *            the timeRangeStart to set
	 */
	@JsonProperty("TimeRangeStart")
	public void setTimeRangeStart(LocalDateTime timeRangeStart) {
		this.timeRangeStart = timeRangeStart;
	}

	/**
	 * @return the timeRangeEnd
	 */
	@JsonProperty("TimeRangeEnd")
	public LocalDateTime getTimeRangeEnd() {
		return timeRangeEnd;
	}

	/**
	 * @param timeRangeEnd
	 *            the timeRangeEnd to set
	 */
	@JsonProperty("TimeRangeEnd")
	public void setTimeRangeEnd(LocalDateTime timeRangeEnd) {
		this.timeRangeEnd = timeRangeEnd;
	}

	/**
	 * @return the valueRangeStart
	 */
	@JsonProperty("ValueRangeStart")
	public Long getValueRangeStart() {
		return valueRangeStart;
	}

	/**
	 * @param valueRangeStart
	 *            the valueRangeStart to set
	 */
	@JsonProperty("ValueRangeStart")
	public void setValueRangeStart(Long valueRangeStart) {
		this.valueRangeStart = valueRangeStart;
	}

	/**
	 * @return the valueRangeEnd
	 */
	@JsonProperty("ValueRangeEnd")
	public Long getValueRangeEnd() {
		return valueRangeEnd;
	}

	/**
	 * @param valueRangeEnd
	 *            the valueRangeEnd to set
	 */
	@JsonProperty("ValueRangeEnd")
	public void setValueRangeEnd(Long valueRangeEnd) {
		this.valueRangeEnd = valueRangeEnd;
	}

	/**
	 * @return the countInput
	 */
	@JsonProperty("CountInput")
	public Integer getCountInput() {
		return countInput;
	}

	/**
	 * @param countInput
	 *            the countInput to set
	 */
	@JsonProperty("CountInput")
	public void setCountInput(Integer countInput) {
		this.countInput = countInput;
	}

	/**
	 * @return the countInsert
	 */
	@JsonProperty("CountInsert")
	public Integer getCountInsert() {
		return countInsert;
	}

	/**
	 * @param countInsert
	 *            the countInsert to set
	 */
	@JsonProperty("CountInsert")
	public void setCountInsert(Integer countInsert) {
		this.countInsert = countInsert;
	}

	/**
	 * @return the countUpdate
	 */
	@JsonProperty("CountUpdate")
	public Integer getCountUpdate() {
		return countUpdate;
	}

	/**
	 * @param countUpdate
	 *            the countUpdate to set
	 */
	@JsonProperty("CountUpdate")
	public void setCountUpdate(Integer countUpdate) {
		this.countUpdate = countUpdate;
	}

	/**
	 * @return the countReject
	 */
	@JsonProperty("CountReject")
	public Integer getCountReject() {
		return countReject;
	}

	/**
	 * @param countReject
	 *            the countReject to set
	 */
	@JsonProperty("CountReject")
	public void setCountReject(Integer countReject) {
		this.countReject = countReject;
	}

	/**
	 * @return the countDelete
	 */
	@JsonProperty("CountDelete")
	public Integer getCountDelete() {
		return countDelete;
	}

	/**
	 * @param countDelete
	 *            the countDelete to set
	 */
	@JsonProperty("CountDelete")
	public void setCountDelete(Integer countDelete) {
		this.countDelete = countDelete;
	}

	/**
	 * @return the jobStarted
	 */
	@JsonProperty("JobStarted")
	public LocalDateTime getJobStarted() {
		return jobStarted;
	}

	/**
	 * @param jobStarted
	 *            the jobStarted to set
	 */
	@JsonProperty("JobStarted")
	public void setJobStarted(LocalDateTime jobStarted) {
		this.jobStarted = jobStarted;
	}

	/**
	 * @return the jobEnded
	 */
	@JsonProperty("JobEnded")
	public LocalDateTime getJobEnded() {
		return jobEnded;
	}

	/**
	 * @param jobEnded
	 *            the jobEnded to set
	 */
	@JsonProperty("JobEnded")
	public void setJobEnded(LocalDateTime jobEnded) {
		this.jobEnded = jobEnded;
	}

	/**
	 * @return the jobResult
	 */
	@JsonProperty("JobResult")
	public String getJobResult() {
		return jobResult;
	}

	/**
	 * @param jobResult
	 *            the jobResult to set
	 */
	@JsonProperty("JobResult")
	public void setJobResult(String jobResult) {
		this.jobResult = jobResult;
	}

	/**
	 * @return the returnCode
	 */
	@JsonProperty("ReturnCode")
	public Integer getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode
	 *            the returnCode to set
	 */
	@JsonProperty("ReturnCode")
	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the returnMessage
	 */
	@JsonProperty("ReturnMessage")
	public String getReturnMessage() {
		return returnMessage;
	}

	/**
	 * @param returnMessage
	 *            the returnMessage to set
	 */
	@JsonProperty("ReturnMessage")
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	/**
	 * @return the hostName
	 */
	@JsonProperty("HostName")
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	@JsonProperty("HostName")
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the hostPid
	 */
	@JsonProperty("HostPID")
	public Integer getHostPid() {
		return hostPid;
	}

	/**
	 * @param hostPid
	 *            the hostPid to set
	 */
	@JsonProperty("HostPID")
	public void setHostPid(Integer hostPid) {
		this.hostPid = hostPid;
	}

	/**
	 * @return the hostUser
	 */
	@JsonProperty("HostUser")
	public String getHostUser() {
		return hostUser;
	}

	/**
	 * @param hostUser
	 *            the hostUser to set
	 */
	@JsonProperty("HostUser")
	public void setHostUser(String hostUser) {
		this.hostUser = hostUser;
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
		return String.format("Job[id='%s', process='%s', name='%s', project='%s', item='%s']", getJobInstanceId(),
				getProcessInstanceId(), getJobName(), getProjectName(), getWorkItem());
	}
}