package net.oopscraft.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import net.oopscraft.application.security.AuthenticationFilter;
import net.oopscraft.application.security.AuthenticationHandler;
import net.oopscraft.application.security.AuthenticationProvider;
import net.oopscraft.application.security.SecurityPolicy;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ApplicationWebSecurityContext {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ApplicationWebSecurityContext.class);
	
    @Value("${application.config.securityPolicy}")
    private SecurityPolicy securityPolicy;
    
    @Autowired
    private AuthenticationProvider authenticationProvider;
    
    @Autowired
    private AuthenticationHandler authenticationHandler;
    
    @Autowired
    private AuthenticationFilter authenticationFilter;

    /**
     * Constructor
     */
	ApplicationWebSecurityContext(){
		LOGGER.warn("ApplicationWebSecurityContext.securityPolicy[{}]", securityPolicy);
	}
    
    /**
     * Static resource security configuration
     */
    @Configuration
    @Order(1)
    public class StaticWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    	protected void configure(HttpSecurity http) throws Exception {
    		http
    		.antMatcher("/static/**")
	    		.authorizeRequests()
	    		.anyRequest()
	    		.permitAll();
        }
    }
    
    /**
     * Administrator security configuration
     */
    @Configuration
    @Order(2)
    public class AdminWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    	protected void configure(HttpSecurity http) throws Exception {
    		http
    		.antMatcher("/admin/**")
	    		.authorizeRequests()
	    		.anyRequest()
	    		.authenticated()
	    		.and()
	    	.authenticationProvider(authenticationProvider)
	    	.addFilterAfter(authenticationFilter, ChannelProcessingFilter.class)
    		.formLogin()
				.loginPage("/admin/login")
				.loginProcessingUrl("/admin/doLogin")
				.usernameParameter("id")
				.passwordParameter("password")
				.successHandler(authenticationHandler)
				.failureHandler(authenticationHandler)
				.permitAll()
				.and()
			.logout()
				.logoutUrl("/admin/logout")
				.logoutSuccessHandler(authenticationHandler)
				.logoutSuccessUrl("/admin/login")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.permitAll();
			;
        }
    }

    /**
     * API security configuration
     */
    @Configuration
    @Order(3)
    public class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    	protected void configure(HttpSecurity http) throws Exception {
    		if(securityPolicy != SecurityPolicy.ANONYMOUS) {
	    		http.antMatcher("/api/**")
		    		.authorizeRequests()
		    		.anyRequest()
		    		.authenticated()
	    		.and()
	    			.httpBasic();
    		}
        }
    }

    /**
     * Global security configuration
     */
    @Configuration
    public class GlobalWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    	protected void configure(HttpSecurity http) throws Exception {
    		if(securityPolicy != SecurityPolicy.ANONYMOUS) {
	    		http
	    		.antMatcher("/**")
		    		.authorizeRequests()
		    		.anyRequest()
		    		.authenticated()
		    		.and()
		    	.authenticationProvider(authenticationProvider)
		    	.addFilterAfter(authenticationFilter, ChannelProcessingFilter.class)
	    		.formLogin()
					.loginPage("/user/login")
					.loginProcessingUrl("/user/doLogin")
					.usernameParameter("id")
					.passwordParameter("password")
					.successHandler(authenticationHandler)
					.failureHandler(authenticationHandler)
					.permitAll()
					.and()
				.logout()
					.logoutUrl("/user/logout")
					.logoutSuccessHandler(authenticationHandler)
					.logoutSuccessUrl("/user/login")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID")
					.permitAll();
				;
    		}
        }
    }

}