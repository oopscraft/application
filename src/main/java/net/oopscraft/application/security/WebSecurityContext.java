package net.oopscraft.application.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityContext extends WebSecurityConfigurerAdapter {
	
	WebSecurityContext(){
		System.err.println("################ ApplicationWebSecurityContext");
	}
		
    @Autowired
    private AuthenticationProvider authenticationProvider;
    
    @Autowired
    private AuthenticationHandler authenticationHandler;
    
    @Autowired
    private AuthenticationFilter authenticationFilter;
	
    @Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();
		
		// Defines security rule
		http.authorizeRequests().antMatchers("/admin","/admin/**").authenticated();
		http.authorizeRequests().antMatchers("/user","/user/**").authenticated();
		http.authorizeRequests().antMatchers("/security","/security/**").permitAll();

		// Adds authentication handler
		http.authenticationProvider(authenticationProvider);
		
		// Adds custom security filter
		http.addFilterAfter(authenticationFilter, ChannelProcessingFilter.class);

		// Defines login and logout
		http.formLogin()
			.loginPage("/security/login")
			.loginProcessingUrl("/security/doLogin")
			.usernameParameter("id")
			.passwordParameter("password")
			.successHandler(authenticationHandler)
			.failureHandler(authenticationHandler);
		http.logout().invalidateHttpSession(true)
			.logoutUrl("/security/logout");

	}

}