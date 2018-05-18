package de.hits.jobinstance.common.utils.test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.hits.jobinstance.common.data.JobInstanceCounter;
import de.hits.jobinstance.common.data.JobInstanceJob;

/**
 * 
 * @author André Hermann
 * @since 08.02.2018
 * @version 1.0
 */
public class IntegrationTestUtil {

	public static byte[] convertObjectToJsonBytes(Object mockResource) throws JsonProcessingException {
		ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
				.serializationInclusion(JsonInclude.Include.NON_NULL)
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();

		return mapper.writeValueAsBytes(mockResource);
	}

	public static JobInstanceJob convertResponseToJobInstanceJson(MvcResult response) throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException {
		ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
				.serializationInclusion(JsonInclude.Include.NON_NULL)
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();

		return mapper.readValue(response.getResponse().getContentAsString(), JobInstanceJob.class);
	}

	public static List<JobInstanceJob> convertResponseToJobInstanceJsonList(MvcResult response)
			throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException {
		ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
				.serializationInclusion(JsonInclude.Include.NON_NULL)
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();

		return Arrays.asList(mapper.readValue(response.getResponse().getContentAsString(), JobInstanceJob[].class));
	}

	public static JobInstanceCounter convertResponseToJobCounterJson(MvcResult response)
			throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException {
		ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
				.serializationInclusion(JsonInclude.Include.NON_NULL)
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();

		return mapper.readValue(response.getResponse().getContentAsString(), JobInstanceCounter.class);
	}

	public static List<JobInstanceCounter> convertResponseToJobCounterJsonList(MvcResult response)
			throws JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException {
		ObjectMapper mapper = Jackson2ObjectMapperBuilder.json()
				.serializationInclusion(JsonInclude.Include.NON_NULL)
				.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.build();

		return Arrays.asList(mapper.readValue(response.getResponse().getContentAsString(), JobInstanceCounter[].class));
	}
}