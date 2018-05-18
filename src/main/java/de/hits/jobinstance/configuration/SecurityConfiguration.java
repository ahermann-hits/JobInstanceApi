package de.hits.jobinstance.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import de.hits.jobinstance.common.PasswordGenerator;
import de.hits.jobinstance.common.utils.StringUtils;

/**
 * Web security configuration class for this microservice.<br>
 * <br>
 * Two types of user-roles are supported user and admin. One administrator can
 * be defined and a list of user accounts. <br>
 * <br>
 * It loads the users configured in the property file and configures the
 * authorisation of the request paths. The functional service functions are
 * provided for the user-role, the management service functions are provided for
 * the admin-role and the documentation functions are provided without
 * authorisation for everyone.<br>
 * <br>
 * This configuration requires the property file
 * '/var/data/microservices/ConflictDetection/user-&lt;profile&gt;.properties'.
 * 
 * @author André Hermann
 * @since 08.03.2018
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@PropertySources({
		@PropertySource(value = "classpath:config/user-${spring.profiles.active}.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "file:./user-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String ROLE_ADMIN = "ADMIN";
	private static final String ROLE_USER = "USER";

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

	@Autowired
	private PasswordGenerator pwdGenerator;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests()
				.antMatchers("/v2/api-docs").permitAll()			// swagger api doc to all
				.antMatchers("/swagger-resources/**").permitAll()
				.antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/error/**").permitAll()				// error page to all
				.antMatchers("/job/api/**").hasRole(ROLE_USER)		// service functions to users
				.antMatchers("/**").hasRole(ROLE_ADMIN)				// all other functions to admin
				.and().csrf().disable()
				.headers().frameOptions().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		String alpha = PasswordGenerator.alpha;
		String alphaCaps = PasswordGenerator.alphaCaps;
		String numeric = PasswordGenerator.numeric;
		String special = null;

		// Split the configured admin account.
		String[] adminProperties = admin.split(":");
		String adminUser = adminProperties[0];
		String adminPwd = adminProperties[1];

		// If nothing is configured for the admin account, set the default name and
		// generate a random password for this run of the application. If the user name
		// is set, but no password defined the empty password will be replaced by a
		// random generated password.
		adminUser = StringUtils.isEmpty(adminUser) ? "admin" : adminUser;
		boolean replaceAdminPassword = StringUtils.isEmpty(adminPwd);
		if (replaceAdminPassword) {
			adminPwd = pwdGenerator.generatePassword(64, alpha, alphaCaps, numeric, special);
		}

		UserDetailsManagerConfigurer<AuthenticationManagerBuilder, InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>>.UserDetailsBuilder roles = auth
				.inMemoryAuthentication().withUser(adminUser).password(adminPwd).roles(ROLE_ADMIN);

		// Print the admin account to std:out
		System.out.println("");
		System.out.println("Using security accounts: ");
		System.out.println(
				"  Admin:  " + adminUser + " : " + adminPwd + (replaceAdminPassword ? " (replaced)" : " (configured)"));
		System.out.println("  Users:");

		for (String user : users) {
			String[] keyValue = user.split(":");
			String userName = keyValue[0];
			String userPwd = keyValue[1];

			// If the password of the actual looped user is empty, it will be replaced by a
			// random generated password.
			boolean replaceUserPassword = StringUtils.isEmpty(userPwd);
			if (replaceUserPassword) {
				userPwd = pwdGenerator.generatePassword(64, alpha, alphaCaps, numeric, special);
			}

			roles.and().withUser(userName).password(userPwd).roles(ROLE_USER);

			// Print the actual looped user account to std:out
			System.out.println("    User: " + userName + " : " + userPwd
					+ (replaceUserPassword ? " (replaced)" : " (configured)"));
		}

		System.out.println("");
	}
}