package de.hits.jobinstance.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import de.hits.jobinstance.JobInstanceApiTest;
import de.hits.jobinstance.common.data.JobInstanceCounter;
import de.hits.jobinstance.common.data.JobInstanceJob;
import de.hits.jobinstance.common.data.JobInstanceStatus;
import de.hits.jobinstance.common.utils.test.IntegrationTestUtil;

/**
 * 
 * @author André Hermann
 * @since 09.02.2018
 * @version 1.0
 */
public class JobInstanceCounterControllerTest extends JobInstanceApiTest {

	@Test
	public void getAllJobCounters() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/job/api/counter"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();

		List<JobInstanceCounter> responseList = IntegrationTestUtil.convertResponseToJobCounterJsonList(result);
		assertTrue(responseList.size() > 0);
	}

	@Test
	public void getOneJobInstanceCounter() throws Exception {
		MvcResult resultJob = this.mockMvc.perform(get("/job/api/status"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();

		JobInstanceJob jobToTest = IntegrationTestUtil.convertResponseToJobInstanceJsonList(resultJob).stream().filter(job -> {
			JobInstanceStatus currentJob = job.getCurrentJob();
			if (currentJob != null && currentJob.getCountInsert() != null && currentJob.getCountInsert() > 0) {
				return true;
			}
			return false;
		}).findFirst().get();
		assertNotNull(jobToTest);

		JobInstanceStatus currentJob = jobToTest.getCurrentJob();
		String jobInstanceIdToTest = "" + currentJob.getJobInstanceId();

		MvcResult resultCounter = this.mockMvc.perform(get("/job/api/counter").param("job", jobInstanceIdToTest))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andReturn();

		List<JobInstanceCounter> counterList = IntegrationTestUtil.convertResponseToJobCounterJsonList(resultCounter);
		List<JobInstanceCounter> counterFiltered = counterList.stream().filter(counter -> counter.getJobInstanceId() == currentJob.getJobInstanceId()).collect(Collectors.toList());
		assertEquals(counterList.size(), counterFiltered.size());
	}
}