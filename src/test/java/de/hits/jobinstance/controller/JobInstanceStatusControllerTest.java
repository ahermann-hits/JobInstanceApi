package de.hits.jobinstance.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import de.hits.jobinstance.JobInstanceApiTest;
import de.hits.jobinstance.common.data.JobInstanceJob;
import de.hits.jobinstance.common.data.JobInstanceStatus;
import de.hits.jobinstance.common.utils.test.IntegrationTestUtil;

/**
 * 
 * @author André Hermann
 * @since 09.02.2018
 * @version 1.0
 */
public class JobInstanceStatusControllerTest extends JobInstanceApiTest {

	@Test
	public void getAllJobInstances() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/job/api/status"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();

		List<JobInstanceJob> responseList = IntegrationTestUtil.convertResponseToJobInstanceJsonList(result);
		assertTrue(responseList.size() > 0);
	}

	@Test
	public void getOneJobInstance() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/job/api/status"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();

		List<JobInstanceJob> responseList = IntegrationTestUtil.convertResponseToJobInstanceJsonList(result);
		int responseSize = responseList.size();
		assertTrue(responseSize > 0);

		Random r = new Random();
		int randomInt = r.nextInt(responseSize);

		JobInstanceJob jobToTest = responseList.get(randomInt);
		JobInstanceStatus currentJob = jobToTest.getCurrentJob();

		this.mockMvc.perform(get("/job/api/status/{id}", currentJob.getJobInstanceId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.CurrentJob.JobInstanceId").value(currentJob.getJobInstanceId()))
				.andExpect(jsonPath("$.CurrentJob.ProjectName").value(currentJob.getProjectName()))
				.andExpect(jsonPath("$.CurrentJob.JobName").value(currentJob.getJobName()))
				.andExpect(jsonPath("$.CurrentJob.HostName").value(currentJob.getHostName()))
				.andExpect(jsonPath("$.CurrentJob.HostPID").value(currentJob.getHostPid()))
				.andExpect(jsonPath("$.CurrentJob.HostUser").value(currentJob.getHostUser()))
				.andExpect(jsonPath("$.CurrentJob.JobGUID").isString())
				.andExpect(jsonPath("$.CurrentJob.JobStarted").isNotEmpty())
				.andDo(print())
				.andReturn();
	}
}