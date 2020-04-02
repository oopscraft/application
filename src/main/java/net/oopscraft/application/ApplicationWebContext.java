package net.oopscraft.application;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.security.AuthenticationFilter;
import net.oopscraft.application.security.AuthenticationHandler;
import net.oopscraft.application.security.AuthenticationProvider;
import net.oopscraft.application.security.SecurityPolicy;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@ComponentScan(
	nameGenerator = net.oopscraft.application.core.spring.FullBeanNameGenerator.class
	,excludeFilters = @Filter(type=FilterType.ANNOTATION, value= {
		Configuration.class
	})
)
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableSwagger2
public class ApplicationWebContext implements WebMvcConfigurer {
	
    @Value("${application.securityPolicy:AUTHENTICATED}")
    private SecurityPolicy securityPolicy;
    
    AuthenticationProvider authenticationProvider;
    AuthenticationHandler authenticationHandler;
    AuthenticationFilter authenticationFilter;
	
	ThymeleafViewResolver viewResolver;
	SpringResourceTemplateResolver templateResolver;
	TemplateEngine templateEngine;
	
	/**
	 * Enables DefaultServletHandlerConfigurer
	 */
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
	/**
	 * Creates AuthenticationProvider
	 * @return
	 * @throws Exception
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() throws Exception {
		authenticationProvider = new AuthenticationProvider();
		return authenticationProvider;
	}
	
	/**
	 * Security access handler implementations.
	 * (AuthenticationSuccessHandler, AuthenticationFailureHandler, AuthenticationEntryPoint, AccessDeniedHandler, LogoutSuccessHandler)
	 * @return
	 * @throws Exception
	 */
	@Bean
	public AuthenticationHandler authenticationHandler() throws Exception {
		authenticationHandler = new AuthenticationHandler();
		return authenticationHandler;
	}
	
	/**
	 * Creates AuthenticationFilter
	 * @return
	 * @throws Exception
	 */
	@Bean
	public AuthenticationFilter authenticationFilter() throws Exception {
		authenticationFilter = new AuthenticationFilter();
		return authenticationFilter;
	}

    /**
     * Static resource security configuration
     */
    @Configuration
    @Order(1)
    public class StaticWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    	protected void configure(HttpSecurity http) throws Exception {
    		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
    @DependsOn({
    	 "authenticationProvider"
    	,"authenticationHandler" 
    	,"authenticationFilter"
    })
    public class AdminWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    	protected void configure(HttpSecurity http) throws Exception {
    		http.antMatcher("/admin/**")
	    		.authorizeRequests()
	    		.anyRequest()
	    		.authenticated()
	    		.and()
    		.sessionManagement()
    			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    			.and()
    		.csrf()
    			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
    			.and()
	    	.addFilterAfter(authenticationFilter, SecurityContextPersistenceFilter.class)
    		.formLogin()
				.loginPage("/admin/login")
				.loginProcessingUrl("/admin/doLogin")
				.usernameParameter("id")
				.passwordParameter("password")
				.successHandler(authenticationHandler)
				.failureHandler(authenticationHandler)
				.permitAll()
				.and()
	    	.authenticationProvider(authenticationProvider)
	    	.exceptionHandling()
    			.authenticationEntryPoint(authenticationHandler)
    			.accessDeniedHandler(authenticationHandler)
	    		.and()
			.logout()
				.logoutUrl("/admin/logout")
				.logoutSuccessUrl("/admin/login")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
				.logoutSuccessHandler(authenticationHandler)
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
    		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
    		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    		if(securityPolicy != SecurityPolicy.ANONYMOUS) {
	    		http
	    		.antMatcher("/**")
		    		.authorizeRequests()
		    		.anyRequest()
		    		.authenticated()
		    		.and()
	    		.sessionManagement()
	    			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    			.and()
	    		.csrf()
	    			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	    			.and()
		    	.addFilterAfter(authenticationFilter, SecurityContextPersistenceFilter.class)
	    		.formLogin()
					.loginPage("/user/login")
					.loginProcessingUrl("/user/doLogin")
					.usernameParameter("id")
					.passwordParameter("password")
					.successHandler(authenticationHandler)
					.failureHandler(authenticationHandler)
					.permitAll()
					.and()
			    	.authenticationProvider(authenticationProvider)
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

	/**
	 * Configures JSON message converter.
	 */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {

        // form
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        messageConverters.add(formHttpMessageConverter);

        // JSON message
    	MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
    	jsonMessageConverter.setObjectMapper(JsonConverter.getObjectMapper());
    	messageConverters.add(jsonMessageConverter);
    	
    	// text
    	StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
    	stringHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
        List<MediaType> stringSupportedMediaTypes = new ArrayList<MediaType>();
        stringSupportedMediaTypes.add(MediaType.TEXT_PLAIN);
        stringHttpMessageConverter.setSupportedMediaTypes(stringSupportedMediaTypes);
        messageConverters.add(stringHttpMessageConverter);

    	// Binary message converter
        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        List<MediaType> byteArraySupportedMediaTypes = new ArrayList<MediaType>();
        byteArraySupportedMediaTypes.add(MediaType.IMAGE_PNG);
        byteArraySupportedMediaTypes.add(MediaType.IMAGE_JPEG);
        byteArraySupportedMediaTypes.add(MediaType.IMAGE_GIF);
        byteArrayHttpMessageConverter.setSupportedMediaTypes(byteArraySupportedMediaTypes);
        messageConverters.add(byteArrayHttpMessageConverter);
    }
	
    /**
     * Adds custom intercepter
     */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("__lang");
		registry.addInterceptor(localeChangeInterceptor);
	}
	
    @Bean
    @DependsOn({"templateEngine"})
    public ViewResolver thymeleafViewResolver() {
        viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setViewNames(new String[]{"*.html"});
        viewResolver.setOrder(1);
        return viewResolver;
    }
    
    @Bean
    @DependsOn({"templateResolver"})
    public TemplateEngine templateEngine() {
        templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new LayoutDialect());
        return templateEngine;
    }
	
    @Bean
    public ITemplateResolver templateResolver() {
        templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    
	/**
	 * Creates JSTL view resolver bean.
	 * @return
	 * @throws Exception
	 */
	@Bean
    public UrlBasedViewResolver jstlViewResolver() throws Exception {
		InternalResourceViewResolver jstlViewResolver = new InternalResourceViewResolver();
        jstlViewResolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        jstlViewResolver.setPrefix("/WEB-INF/views/");
        jstlViewResolver.setViewNames("*.jsp");
        jstlViewResolver.setOrder(2);
        return jstlViewResolver;
    }
	
	
	@Bean
	public CommonsMultipartResolver multipartResolver() throws Exception {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSizePerFile(10485760);	// limits 10MB
		return multipartResolver;
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2) 
				.select()                                 
				.apis(RequestHandlerSelectors.any())             
				.paths(PathSelectors.ant("/api/**/*"))    
				.build();                                          
	}

}
