package de.hits.jobinstance.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@Entity
@Table(name = "job_instance_status", schema = "manage")
public class JobInstanceStatus {

	@Id
	@Column(name = "JOB_INSTANCE_ID", nullable = false, insertable = true, updatable = false)
	private long jobInstanceId;
	@Column(name = "PROCESS_INSTANCE_ID", nullable = true, insertable = true, updatable = false)
	private Long processInstanceId;

	@Column(name = "JOB_PROJECT", nullable = true, insertable = true, updatable = false, length = 128)
	private String projectName;
	@Column(name = "JOB_NAME", nullable = false, insertable = true, updatable = false, length = 255)
	private String jobName;
	@Column(name = "JOB_GUID", nullable = false, insertable = true, updatable = false, length = 50)
	private String jobGUID;

	@Column(name = "WORK_ITEM", nullable = true, insertable = true, updatable = false, length = 1024)
	private String workItem;

	@Column(name = "TIME_RANGE_START", nullable = true, insertable = true, updatable = true)
	private Timestamp timeRangeStart;
	@Column(name = "TIME_RANGE_END", nullable = true, insertable = true, updatable = true)
	private Timestamp timeRangeEnd;
	@Column(name = "VALUE_RANGE_START", nullable = true, insertable = true, updatable = true)
	private Long valueRangeStart;
	@Column(name = "VALUE_RANGE_END", nullable = true, insertable = true, updatable = true)
	private Long valueRangeEnd;

	@Column(name = "COUNT_INPUT", nullable = true, insertable = true, updatable = true)
	private Integer countInput;
	// FIXME: count_output muss zu count_insert werden.
	@Column(name = "COUNT_OUTPUT", nullable = true, insertable = true, updatable = true)
	private Integer countOutput;
	@Column(name = "COUNT_UPDATE", nullable = true, insertable = true, updatable = true)
	private Integer countUpdate;
	@Column(name = "COUNT_REJECT", nullable = true, insertable = true, updatable = true)
	private Integer countReject;
	@Column(name = "COUNT_DELETE", nullable = true, insertable = true, updatable = true)
	private Integer countDelete;

	@Column(name = "JOB_STARTED", nullable = false, insertable = true, updatable = false)
	private Timestamp jobStarted;
	@Column(name = "JOB_ENDED", nullable = true, insertable = true, updatable = true)
	private Timestamp jobEnded;
	@Column(name = "JOB_RESULT", nullable = true, insertable = true, updatable = true, length = 1024)
	private String jobResult;

	@Column(name = "RETURN_CODE", nullable = true, insertable = true, updatable = true)
	private Integer returnCode;
	@Column(name = "RETURN_MESSAGE", nullable = true, insertable = true, updatable = true, length = 1024)
	private String returnMessage;

	@Column(name = "HOST_NAME", nullable = true, insertable = true, updatable = true, length = 255)
	private String hostName;
	@Column(name = "HOST_PID", nullable = true, insertable = true, updatable = true)
	private Integer hostPid;
	@Column(name = "HOST_USER", nullable = true, insertable = true, updatable = true, length = 128)
	private String hostUser;

	public JobInstanceStatus() {}

	@PrePersist
	public void prePersist() {
		setJobStarted(new Timestamp(new java.util.Date().getTime()));
	}

	@PreUpdate
	public void preUpdate() {
		setJobEnded(new Timestamp(new java.util.Date().getTime()));
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
	 * @return the processInstanceId
	 */
	public Long getProcessInstanceId() {
		return processInstanceId;
	}

	/**
	 * @param processInstanceId
	 *            the processInstanceId to set
	 */
	public void setProcessInstanceId(Long processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	/**
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return the jobName
	 */
	public String getJobName() {
		return jobName;
	}

	/**
	 * @param jobName
	 *            the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * @return the jobGUID
	 */
	public String getJobGUID() {
		return jobGUID;
	}

	/**
	 * @param jobGUID
	 *            the jobGUID to set
	 */
	public void setJobGUID(String jobGUID) {
		this.jobGUID = jobGUID;
	}

	/**
	 * @return the workItem
	 */
	public String getWorkItem() {
		return workItem;
	}

	/**
	 * @param workItem
	 *            the workItem to set
	 */
	public void setWorkItem(String workItem) {
		this.workItem = workItem;
	}

	/**
	 * @return the timeRangeStart
	 */
	public Timestamp getTimeRangeStart() {
		return timeRangeStart;
	}

	/**
	 * @param timeRangeStart
	 *            the timeRangeStart to set
	 */
	public void setTimeRangeStart(Timestamp timeRangeStart) {
		this.timeRangeStart = timeRangeStart;
	}

	/**
	 * @return the timeRangeEnd
	 */
	public Timestamp getTimeRangeEnd() {
		return timeRangeEnd;
	}

	/**
	 * @param timeRangeEnd
	 *            the timeRangeEnd to set
	 */
	public void setTimeRangeEnd(Timestamp timeRangeEnd) {
		this.timeRangeEnd = timeRangeEnd;
	}

	/**
	 * @return the valueRangeStart
	 */
	public Long getValueRangeStart() {
		return valueRangeStart;
	}

	/**
	 * @param valueRangeStart
	 *            the valueRangeStart to set
	 */
	public void setValueRangeStart(Long valueRangeStart) {
		this.valueRangeStart = valueRangeStart;
	}

	/**
	 * @return the valueRangeEnd
	 */
	public Long getValueRangeEnd() {
		return valueRangeEnd;
	}

	/**
	 * @param valueRangeEnd
	 *            the valueRangeEnd to set
	 */
	public void setValueRangeEnd(Long valueRangeEnd) {
		this.valueRangeEnd = valueRangeEnd;
	}

	/**
	 * @return the countInput
	 */
	public Integer getCountInput() {
		return countInput;
	}

	/**
	 * @param countInput
	 *            the countInput to set
	 */
	public void setCountInput(Integer countInput) {
		this.countInput = countInput;
	}

	/**
	 * @return the countOutput
	 */
	public Integer getCountOutput() {
		return countOutput;
	}

	/**
	 * @param countOutput
	 *            the countOutput to set
	 */
	public void setCountOutput(Integer countOutput) {
		this.countOutput = countOutput;
	}

	/**
	 * @return the countUpdate
	 */
	public Integer getCountUpdate() {
		return countUpdate;
	}

	/**
	 * @param countUpdate
	 *            the countUpdate to set
	 */
	public void setCountUpdate(Integer countUpdate) {
		this.countUpdate = countUpdate;
	}

	/**
	 * @return the countReject
	 */
	public Integer getCountReject() {
		return countReject;
	}

	/**
	 * @param countReject
	 *            the countReject to set
	 */
	public void setCountReject(Integer countReject) {
		this.countReject = countReject;
	}

	/**
	 * @return the countDelete
	 */
	public Integer getCountDelete() {
		return countDelete;
	}

	/**
	 * @param countDelete
	 *            the countDelete to set
	 */
	public void setCountDelete(Integer countDelete) {
		this.countDelete = countDelete;
	}

	/**
	 * @return the jobStarted
	 */
	public Timestamp getJobStarted() {
		return jobStarted;
	}

	/**
	 * @param jobStarted
	 *            the jobStarted to set
	 */
	public void setJobStarted(Timestamp jobStarted) {
		this.jobStarted = jobStarted;
	}

	/**
	 * @return the jobEnded
	 */
	public Timestamp getJobEnded() {
		return jobEnded;
	}

	/**
	 * @param jobEnded
	 *            the jobEnded to set
	 */
	public void setJobEnded(Timestamp jobEnded) {
		this.jobEnded = jobEnded;
	}

	/**
	 * @return the jobResult
	 */
	public String getJobResult() {
		return jobResult;
	}

	/**
	 * @param jobResult
	 *            the jobResult to set
	 */
	public void setJobResult(String jobResult) {
		this.jobResult = jobResult;
	}

	/**
	 * @return the returnCode
	 */
	public Integer getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode
	 *            the returnCode to set
	 */
	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the returnMessage
	 */
	public String getReturnMessage() {
		return returnMessage;
	}

	/**
	 * @param returnMessage
	 *            the returnMessage to set
	 */
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the hostPid
	 */
	public Integer getHostPid() {
		return hostPid;
	}

	/**
	 * @param hostPid
	 *            the hostPid to set
	 */
	public void setHostPid(Integer hostPid) {
		this.hostPid = hostPid;
	}

	/**
	 * @return the hostUser
	 */
	public String getHostUser() {
		return hostUser;
	}

	/**
	 * @param hostUser
	 *            the hostUser to set
	 */
	public void setHostUser(String hostUser) {
		this.hostUser = hostUser;
	}

	@Override
	public String toString() {
		return String.format("Job[id='%s', process='%s', name='%s', item='%s']", getJobInstanceId(),
				getProcessInstanceId(), getJobName(), getWorkItem());
	}
}