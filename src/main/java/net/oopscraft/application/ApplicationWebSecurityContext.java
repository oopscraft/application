package net.oopscraft.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import net.oopscraft.application.security.AuthenticationFilter;
import net.oopscraft.application.security.AuthenticationHandler;
import net.oopscraft.application.security.AuthenticationProvider;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ApplicationWebSecurityContext extends WebSecurityConfigurerAdapter {
	
	ApplicationWebSecurityContext(){
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
		http.antMatcher("/admin/**/*");
		http.csrf().disable();


		http.authorizeRequests().antMatchers("/admin/login").permitAll();
		http.authorizeRequests().antMatchers("/admin/login/*").permitAll();
		http.authorizeRequests().antMatchers("/admin/**/*").permitAll();

		http.authenticationProvider(authenticationProvider);
		http.addFilterAfter(authenticationFilter, ChannelProcessingFilter.class);

		http.formLogin().loginPage("/admin/login")
			.loginProcessingUrl("/admin/login/processing")
			.usernameParameter("id")
			.passwordParameter("password")
			.successHandler(authenticationHandler)
			.failureHandler(authenticationHandler);
		http.logout().invalidateHttpSession(true)
			.logoutUrl("/admin");

	}

}