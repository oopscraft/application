package net.oopscraft.application;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.LocaleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import net.oopscraft.application.api.ApiWebSocketHandler;
import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.core.Pagination;
import net.oopscraft.application.security.AuthenticationFilter;
import net.oopscraft.application.security.AuthenticationHandler;
import net.oopscraft.application.security.AuthenticationProvider;
import net.oopscraft.application.security.SecurityPolicy;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
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
@EnableWebSocket
@EnableScheduling
@EnableSwagger2
public class ApplicationWebContext implements WebMvcConfigurer, WebSocketConfigurer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationWebContext.class);
	
	public static final String ACCESS_TOKEN_HEADER_NAME = "X-ACCESS-TOKEN";
	public static final String CSRF_TOKEN_HEADER_NAME = "X-CSRF-TOKEN";
	public static final String LOCALE_HEADER_NAME = "X-LOCALE";
	
    static AuthenticationProvider authenticationProvider;
    static AuthenticationHandler authenticationHandler;
    static AuthenticationFilter authenticationFilter;
    static CookieCsrfTokenRepository cookieCsrfTokenRepository;
    static ThymeleafViewResolver viewResolver;
    static 	SpringResourceTemplateResolver templateResolver;
    static 	TemplateEngine templateEngine;

    @Value("${application.securityPolicy:AUTHENTICATED}")
    private SecurityPolicy securityPolicy;
    
	@Autowired
	private Environment environment;
    
	@Autowired
	ApiWebSocketHandler apiWebSocketHandler;
	
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
	
	@Bean
	public CookieCsrfTokenRepository csrfTokenRepository() throws Exception {
		cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
		cookieCsrfTokenRepository.setCookieName(CSRF_TOKEN_HEADER_NAME);
		cookieCsrfTokenRepository.setHeaderName(CSRF_TOKEN_HEADER_NAME);
		return cookieCsrfTokenRepository;
	}
	
	@Bean
	public CookieLocaleResolver localeResolver() throws Exception {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		try {
			localeResolver.setCookieName(LOCALE_HEADER_NAME);
			String locales = environment.getProperty("application.locales");
			String defaultLocale = locales.split(",")[0];
			localeResolver.setDefaultLocale(LocaleUtils.toLocale(defaultLocale));
		}catch(Exception ignore) {
			LOGGER.warn(ignore.getMessage());
			localeResolver.setDefaultLocale(Locale.getDefault());	
		}
		return localeResolver;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
        	.addResourceLocations("/static/")
        	.setCachePeriod(3600)
        	.resourceChain(true)
        	.addResolver(new GzipResourceResolver());
	}
	
    /**
     * Static resource security configuration
     */
    @Configuration
    @Order(1)
    public class StaticWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    	protected void configure(HttpSecurity http) throws Exception {
    		http.antMatcher("/static/**")
	    		.authorizeRequests()
	    		.anyRequest()
	    		.permitAll();
    		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
	    		.authenticated();
    		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    		http.csrf().csrfTokenRepository(csrfTokenRepository());
	    	http.authenticationProvider(authenticationProvider);
	    	http.addFilterAfter(authenticationFilter, SecurityContextPersistenceFilter.class);
	    	http.exceptionHandling().authenticationEntryPoint(authenticationHandler);
    		http.formLogin()
				.loginPage("/admin/login")
				.loginProcessingUrl("/admin/doLogin")
				.usernameParameter("email")
				.passwordParameter("password")
				.successHandler(authenticationHandler)
				.failureHandler(authenticationHandler)
				.permitAll();
			http.logout()
				.logoutUrl("/admin/logout")
				.logoutSuccessUrl("/admin/login")
				.invalidateHttpSession(true)
				.deleteCookies(ACCESS_TOKEN_HEADER_NAME)
				.logoutSuccessHandler(authenticationHandler)
				.permitAll();
        }
    }

    /**
     * API security configuration
     */
    @Configuration
    @Order(3)
    public class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    	protected void configure(HttpSecurity http) throws Exception {
    		
    		// URL ant matcher
    		String antMatcher = "/api/**";
    		
    		// allow anonymous
    		if(securityPolicy == SecurityPolicy.ANONYMOUS) {
	    		http.antMatcher(antMatcher)
		    		.authorizeRequests()
		    		.anyRequest()
		    		.permitAll();
    		}
    		// requests authentication
    		else{
	    		http.antMatcher(antMatcher)
		    		.authorizeRequests()
		    		.anyRequest()
		    		.authenticated();
	    		http.httpBasic();
    		}

			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			http.csrf().disable();
	    	http.addFilterAfter(authenticationFilter, SecurityContextPersistenceFilter.class);
        }
    }

    /**
     * Global security configuration
     */
    @Configuration
    public class GlobalWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
    	protected void configure(HttpSecurity http) throws Exception {
    		
    		// URL ant matcher
    		String antMatcher = "/**";
    		
    		// allow anonymous
    		if(securityPolicy == SecurityPolicy.ANONYMOUS) {
	    		http.antMatcher(antMatcher)
		    		.authorizeRequests()
		    		.anyRequest()
		    		.permitAll();
    		}
    		// requests authentication
    		else{
	    		http.antMatcher(antMatcher)
		    		.authorizeRequests()
		    		.anyRequest()
		    		.authenticated();
    		}
    		
    		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    		http.csrf().csrfTokenRepository(csrfTokenRepository());
    		http.addFilterAfter(authenticationFilter, SecurityContextPersistenceFilter.class);
    		http.authenticationProvider(authenticationProvider);
			http.formLogin()
				.loginPage("/user/login")
				.loginProcessingUrl("/user/doLogin")
				.usernameParameter("username")
				.passwordParameter("password")
				.successHandler(authenticationHandler)
				.failureHandler(authenticationHandler)
				.permitAll();
			http.logout()
				.logoutUrl("/user/logout")
				.logoutSuccessHandler(authenticationHandler)
				.logoutSuccessUrl("/user/login")
				.invalidateHttpSession(true)
				.deleteCookies(ACCESS_TOKEN_HEADER_NAME)
				.permitAll();
        }
    }
    
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
    	
    	// pagination
    	argumentResolvers.add(new HandlerMethodArgumentResolver() {
			@Override
			public boolean supportsParameter(MethodParameter parameter) {
				return parameter.getParameterType().equals(Pagination.class);
			}
			@Override
			public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
				Pagination pagination = new Pagination();
				try { pagination.setPage(Integer.parseInt(webRequest.getParameter("_page"))); }catch(Exception ignore) {}
				try { pagination.setRows(Integer.parseInt(webRequest.getParameter("_rows"))); }catch(Exception ignore) {}
		        return pagination;
			}
    	});
    	
    	// spring-mobile argument resolver
    	argumentResolvers.add(new DeviceHandlerMethodArgumentResolver());
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
		
		// locale change intercepter
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("_lang");
		registry.addInterceptor(localeChangeInterceptor);
		
		// spring-mobile
		registry.addInterceptor(new DeviceResolverHandlerInterceptor());
	}
	
    @Bean
    @DependsOn({"templateEngine"})
    public ViewResolver thymeleafViewResolver() {
        viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine((ISpringTemplateEngine)templateEngine);
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
        templateEngine.addDialect(new SpringSecurityDialect());
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
		multipartResolver.setMaxUploadSizePerFile(1024*1024*1024);	// limits 1Gb
		return multipartResolver;
	}

	@Bean
	public TaskScheduler taskScheduler() {
	    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
	    taskScheduler.setPoolSize(10);
	    taskScheduler.initialize();
	    return taskScheduler;
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(apiWebSocketHandler, "/api/webSocket");
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
