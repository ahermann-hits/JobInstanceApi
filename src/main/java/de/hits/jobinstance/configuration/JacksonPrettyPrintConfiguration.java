package de.hits.jobinstance.configuration;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Web configuration class for this microservice.<br>
 * <br>
 * Changes the Jackson converter to use pretty print by default for JSON output
 * strings.<br>
 * <br>
 * Copied from
 * {@link https://springframework.guru/enable-pretty-print-of-json-with-jackson/}.
 * 
 * @author André Hermann
 * @since 29.05.2018
 * @version 1.0
 */
@Configuration
public class JacksonPrettyPrintConfiguration extends WebMvcConfigurationSupport {

	@Override
	protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		for (HttpMessageConverter<?> converter : converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) converter;
				jacksonConverter.setPrettyPrint(true);
			}
		}
	}
}