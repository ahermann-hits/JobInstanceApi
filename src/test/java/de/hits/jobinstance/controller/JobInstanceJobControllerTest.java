package de.hits.jobinstance.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import de.hits.jobinstance.JobInstanceApiTest;
import de.hits.jobinstance.common.utils.test.IntegrationTestUtil;
import de.hits.jobinstance.data.CatalogJson;
import de.hits.jobinstance.data.JobInstanceCounterJson;
import de.hits.jobinstance.data.JobInstanceJobJson;
import de.hits.jobinstance.data.JobInstanceStatusJson;

/**
 * 
 * @author André Hermann
 * @since 09.02.2018
 * @version 1.0
 */
public class JobInstanceJobControllerTest extends JobInstanceApiTest {

	@Autowired
	private CatalogController catalogController;

	@Test
	public void createJobInstanceValid() throws Exception {
		String projectName = "mockTestProject";
		String jobName = "testCreateJobInstanceValid";
		String hostName = "mockHost";
		int hostPid = 1;
		String hostUser = "mockUser";

		JobInstanceStatusJson mockResource = new JobInstanceStatusJson();
		mockResource.setProjectName(projectName);
		mockResource.setJobName(jobName);
		mockResource.setHostName(hostName);
		mockResource.setHostPid(hostPid);
		mockResource.setHostUser(hostUser);

		this.mockMvc.perform(post("/job/api/job/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(IntegrationTestUtil.convertObjectToJsonBytes(mockResource))
				)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.CurrentJob.JobInstanceId").isNumber())
				.andExpect(jsonPath("$.CurrentJob.ProjectName").value(projectName))
				.andExpect(jsonPath("$.CurrentJob.JobName").value(jobName))
				.andExpect(jsonPath("$.CurrentJob.HostName").value(hostName))
				.andExpect(jsonPath("$.CurrentJob.HostPID").value(hostPid))
				.andExpect(jsonPath("$.CurrentJob.HostUser").value(hostUser))
				.andExpect(jsonPath("$.CurrentJob.JobGUID").isString())
				.andExpect(jsonPath("$.CurrentJob.JobStarted").isNotEmpty())
				.andExpect(jsonPath("$.Counter").doesNotExist())
				.andExpect(jsonPath("$.PreviousJobs").doesNotExist())
				.andDo(print());
	}

	@Test
	public void createJobInstanceAndRead() throws Exception {
		String projectName = "mockTestProject";
		String jobName = "testCreateJobInstanceAndRead";
		String hostName = "mockHost";
		int hostPid = 12093;
		String hostUser = "mockUser";

		JobInstanceStatusJson mockResource = new JobInstanceStatusJson();
		mockResource.setProjectName(projectName);
		mockResource.setJobName(jobName);
		mockResource.setHostName(hostName);
		mockResource.setHostPid(hostPid);
		mockResource.setHostUser(hostUser);

		MvcResult responseCreate = this.mockMvc.perform(post("/job/api/job/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(IntegrationTestUtil.convertObjectToJsonBytes(mockResource))
				)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.CurrentJob.JobInstanceId").isNumber())
				.andExpect(jsonPath("$.CurrentJob.ProjectName").value(projectName))
				.andExpect(jsonPath("$.CurrentJob.JobName").value(jobName))
				.andExpect(jsonPath("$.CurrentJob.HostName").value(hostName))
				.andExpect(jsonPath("$.CurrentJob.HostPID").value(hostPid))
				.andExpect(jsonPath("$.CurrentJob.HostUser").value(hostUser))
				.andExpect(jsonPath("$.CurrentJob.JobGUID").isString())
				.andExpect(jsonPath("$.CurrentJob.JobStarted").isNotEmpty())
				.andExpect(jsonPath("$.Counter").doesNotExist())
				.andExpect(jsonPath("$.PreviousJobs").doesNotExist())
				.andReturn();

		JobInstanceJobJson responseCreateJson = IntegrationTestUtil.convertResponseToJobInstanceJson(responseCreate);
		long jobInstanceId = responseCreateJson.getCurrentJob().getJobInstanceId();

		this.mockMvc.perform(get("/job/api/job/list/{id}", jobInstanceId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.CurrentJob.JobInstanceId").value(jobInstanceId))
				.andExpect(jsonPath("$.CurrentJob.ProjectName").value(projectName))
				.andExpect(jsonPath("$.CurrentJob.JobName").value(jobName))
				.andExpect(jsonPath("$.CurrentJob.HostName").value(hostName))
				.andExpect(jsonPath("$.CurrentJob.HostPID").value(hostPid))
				.andExpect(jsonPath("$.CurrentJob.HostUser").value(hostUser))
				.andExpect(jsonPath("$.CurrentJob.JobGUID").isString())
				.andExpect(jsonPath("$.CurrentJob.JobStarted").isNotEmpty())
				.andExpect(jsonPath("$.Counter").doesNotExist())
				.andExpect(jsonPath("$.PreviousJobs").doesNotExist())
				.andDo(print());
	}

	@Test
	public void createJobInstanceAndUpdate() throws Exception {
		String projectName = "mockTestProject";
		String jobName = "testCreateJobInstanceAndUpdate";
		String hostName = "mockHost";
		int hostPid = 14174;
		String hostUser = "mockUser";

		JobInstanceStatusJson mockResource = new JobInstanceStatusJson();
		mockResource.setProjectName(projectName);
		mockResource.setJobName(jobName);
		mockResource.setHostName(hostName);
		mockResource.setHostPid(hostPid);
		mockResource.setHostUser(hostUser);

		MvcResult responseCreate = this.mockMvc.perform(post("/job/api/job/create")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(IntegrationTestUtil.convertObjectToJsonBytes(mockResource))
				)
				.andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.CurrentJob.JobInstanceId").isNumber())
				.andExpect(jsonPath("$.CurrentJob.ProjectName").value(projectName))
				.andExpect(jsonPath("$.CurrentJob.JobName").value(jobName))
				.andExpect(jsonPath("$.CurrentJob.HostName").value(hostName))
				.andExpect(jsonPath("$.CurrentJob.HostPID").value(hostPid))
				.andExpect(jsonPath("$.CurrentJob.HostUser").value(hostUser))
				.andExpect(jsonPath("$.CurrentJob.JobGUID").isString())
				.andExpect(jsonPath("$.CurrentJob.JobStarted").isNotEmpty())
				.andExpect(jsonPath("$.Counter").doesNotExist())
				.andExpect(jsonPath("$.PreviousJobs").doesNotExist())
				.andReturn();

		JobInstanceJobJson responseCreateJson = IntegrationTestUtil.convertResponseToJobInstanceJson(responseCreate);
		long jobInstanceId = responseCreateJson.getCurrentJob().getJobInstanceId();

		this.mockMvc.perform(get("/job/api/job/list/{id}", jobInstanceId))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print());

		CatalogJson catalogCounterFetch = this.catalogController.findByName("counter_fetch");
		JobInstanceCounterJson counterFetch = new JobInstanceCounterJson(null, jobInstanceId,
				catalogCounterFetch.getCatalogId(), "mockCounter", 10);
		responseCreateJson.addCounter(counterFetch);

		CatalogJson catalogCounterInsert = this.catalogController.findByName("counter_insert");
		JobInstanceCounterJson counterInsert = new JobInstanceCounterJson(null, jobInstanceId,
				catalogCounterInsert.getCatalogId(), "mockCounter", 10);
		responseCreateJson.addCounter(counterInsert);

		this.mockMvc.perform(put("/job/api/job/update/" + jobInstanceId)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(IntegrationTestUtil.convertObjectToJsonBytes(responseCreateJson))
				)
				.andExpect(status().isOk())
				.andDo(print());

		verify(jobService, times(1)).getJobById(jobInstanceId);
		verifyNoMoreInteractions(jobService);
	}
}