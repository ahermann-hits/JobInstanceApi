package de.hits.jobinstance.configuration.properties;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for external configuration properties.<br>
 * <br>
 * 
 * This configuration is bound to the property file
 * '/var/data/JobInstance/api/user-&lt;profile&gt;.properties'.
 * 
 * @author André Hermann
 * @since 11.04.2018
 * @version 1.0
 */
@Configuration
@PropertySource(value = "file:/var/data/JobInstance/api/user-${spring.profiles.active}.properties", ignoreResourceNotFound = false)
@ConfigurationProperties
public class UserProperties {

	/**
	 * The parameter of the administrator account containing user name and password
	 * separated by a double dot. <br>
	 * <br>
	 * Example: admin:hallo
	 */
	@Value("${api.role.admin}")
	private String admin;
	/**
	 * The parameter of all functional users. The value must be a comma separated
	 * list of pairs of user name and password separated by a double dot.<br>
	 * <br>
	 * Example: app1:hallo,app2:world
	 */
	@Value("#{'${api.role.users}'.split(',')}")
	private List<String> users;

	/**
	 * @return the value of the property 'api.role.admin' from the configuration
	 *         file.
	 */
	public String getAdmin() {
		return admin;
	}

	/**
	 * @return the value list of the property 'api.role.users' from the
	 *         configuration file.
	 */
	public List<String> getUsers() {
		return users;
	}
}