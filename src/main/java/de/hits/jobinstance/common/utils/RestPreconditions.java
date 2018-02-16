package de.hits.jobinstance.common.utils;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public class RestPreconditions {

	public static <T> T checkFound(T resource) {
		if (resource == null) {
			throw new ResourceNotFoundException();
		}
		return resource;
	}
}