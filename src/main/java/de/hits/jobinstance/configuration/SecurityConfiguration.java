package de.hits.jobinstance.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
	@Value("${api.role.user}")
	private String user;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests()
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

		String[] userProperties = user.split(":");
		String userName = userProperties[0];
		String userPwd = userProperties[1];

		auth.inMemoryAuthentication()
				.withUser(userName).password(userPwd).roles(ROLE_USER).and()
				.withUser(adminUser).password(adminPwd).roles(ROLE_USER, ROLE_ADMIN);

		System.out.println("");
		System.out.println("Using security accounts:");
		System.out.println("  Admin: " + adminUser + " : " + adminPwd);
		System.out.println("  User:  " + userName + " : " + userPwd);
		System.out.println("");
	}
}