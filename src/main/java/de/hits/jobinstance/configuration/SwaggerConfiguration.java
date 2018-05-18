package de.hits.jobinstance.configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author André Hermann
 * @since 10.02.2018
 * @version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Bean
	public Docket api() {
		// URL: 	http://localhost:8070/v2/api-docs
		//			http://localhost:8070/swagger-ui.html
		// Links:	https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/
		//			https://www.concretepage.com/spring-4/spring-rest-swagger-2-integration-with-annotation-xml-example
		// Publish:	https://dzone.com/articles/auto-publishing-amp-monitoring-apis-with-spring-bo

		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("de.hits.jobinstance"))
				.paths(PathSelectors.regex("/job/api/catalog*|/job/api/job*|/job/api/status*|/job/api/counter*"))
				.build()
				.apiInfo(metaData());
	}

	private ApiInfo metaData() {
		String title = "JobInstance REST API";
		String description = "";
		String version = "0.0.2";
		String termsOfServiceUrl = "";
		Contact contact = new Contact("André Hermann", description, "andre_hermann@t-online.de");
		String license = "Apache License Version 2.0";
		String licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0";

		@SuppressWarnings("rawtypes")
		ArrayList<VendorExtension> vendorExtensions = new ArrayList<VendorExtension>();

		return new ApiInfo(title, description, version, termsOfServiceUrl, contact, license, licenseUrl, vendorExtensions);
	}
}