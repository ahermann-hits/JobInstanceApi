package de.hits.jobinstance.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.hits.jobinstance.common.utils.RestPreconditions;
import de.hits.jobinstance.data.JobInstanceJobJson;
import de.hits.jobinstance.data.JobInstanceStatusJson;
import de.hits.jobinstance.service.JobInstanceService;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@RestController
@RequestMapping("/job/api/status")
public class JobInstanceStatusController {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final static Boolean DEFAULT_SUCCESSFUL = true;
	private final static Boolean DEFAULT_WITH_INPUT = true;
	private final static Boolean DEFAULT_WITH_OUTPUT = true;
	private final static Boolean DEFAULT_FOR_WORKITEM = true;

	@Autowired
	private JobInstanceService jobService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<JobInstanceJobJson> findAll() {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#findAll()");
		}

		return this.jobService.listAllJobs().stream().map(job -> this.jobService.convertJobToJson(job))
				.map(json -> new JobInstanceJobJson(json)).collect(Collectors.toList());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public JobInstanceJobJson findOne(@PathVariable("id") final long id,
			@RequestParam(value = "counter", required = false) final Boolean counter,
			@RequestParam(value = "previous", required = false) final Boolean previousJobs) {
		if (this.log.isTraceEnabled()) {
			this.log.trace(this.getClass().getSimpleName() + "#findOne()");
		}

		JobInstanceStatusJson job = this.jobService
				.convertJobToJson(RestPreconditions.checkFound(this.jobService.getJobById(id)));
		JobInstanceJobJson response = new JobInstanceJobJson(job);

		if (counter != null && counter) {
			response.setCounters(this.jobService.listAllCounterForJob(id).stream()
					.map(entity -> this.jobService.convertCounterToJson(entity)).collect(Collectors.toList()));
		}
		if (previousJobs != null && previousJobs) {
			this.jobService.retrievePreviousInstanceData(response, DEFAULT_SUCCESSFUL, DEFAULT_WITH_INPUT,
					DEFAULT_WITH_OUTPUT, DEFAULT_FOR_WORKITEM);
		}

		return response;
	}
}