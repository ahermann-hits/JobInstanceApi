package de.hits.jobinstance.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.hits.jobinstance.common.utils.RestPreconditions;
import de.hits.jobinstance.data.JobInstanceCounterJson;
import de.hits.jobinstance.service.JobInstanceService;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
@RestController
@RequestMapping("/job/api/counter")
public class JobInstanceCounterController {

	@Autowired
	private JobInstanceService jobService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<JobInstanceCounterJson> findAll(@RequestParam(value = "job", required = false) final Long jobId) {
		if (jobId != null) {
			return jobService.listAllCounterForJob(jobId).stream()
					.map(counter -> jobService.convertCounterToJson(counter)).collect(Collectors.toList());
		} else {
			return jobService.listAllCounter().stream().map(counter -> jobService.convertCounterToJson(counter))
					.collect(Collectors.toList());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public JobInstanceCounterJson findOne(@PathVariable("id") long id) {
		return jobService.convertCounterToJson(RestPreconditions.checkFound(jobService.getCounterById(id)));
	}
}