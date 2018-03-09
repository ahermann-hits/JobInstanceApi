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

/**
 * 
 * @author André Hermann
 * @since 08.03.2018
 * @version 1.0
 */
@PropertySources({
	@PropertySource(value = "classpath:config/user-${spring.profiles.active}.properties", ignoreResourceNotFound = true),
	@PropertySource(value = "file:./user-${spring.profiles.active}.properties", ignoreResourceNotFound = true)
})
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String ROLE_ADMIN = "ADMIN";
	private static final String ROLE_USER = "USER";

	@Value("${api.role.admin}")
	private String admin;
	@Value("#{'${api.role.users}'.split(',')}")
	private List<String> users;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests()
				.antMatchers("/v2/api-docs").permitAll()
				.antMatchers("/swagger-resources/**").permitAll()
				.antMatchers("/swagger-ui.html").permitAll()
				.antMatchers("/error/**").permitAll()
				.antMatchers("/job/api/**").hasRole(ROLE_USER)
				.antMatchers("/**").hasRole(ROLE_ADMIN)
				.and().csrf().disable()
				.headers().frameOptions().disable();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		String[] adminProperties = admin.split(":");
		String adminUser = adminProperties[0];
		String adminPwd = adminProperties[1];
		UserDetailsManagerConfigurer<AuthenticationManagerBuilder, InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>>.UserDetailsBuilder roles = auth
				.inMemoryAuthentication().withUser(adminUser).password(adminPwd).roles(ROLE_ADMIN);

		System.out.println("");
		System.out.println("Using security accounts: ");
		System.out.println("  Admin:  " + adminUser + " : " + adminPwd);
		System.out.println("  Users:");

		for (String user : users) {
			String[] keyValue = user.split(":");
			String userName = keyValue[0];
			String userPwd = keyValue[1];
			roles.and().withUser(userName).password(userPwd).roles(ROLE_USER);

			System.out.println("    User: " + userName + " : " + userPwd);
		}

		System.out.println("");
	}
}