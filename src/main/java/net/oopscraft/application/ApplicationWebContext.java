package net.oopscraft.application;

import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import net.oopscraft.application.core.JsonConverter;
import net.oopscraft.application.monitor.MonitorAgent;
import nz.net.ultraq.thymeleaf.LayoutDialect;

@EnableWebMvc
@ComponentScan(
	basePackages = "net.oopscraft.application",
	nameGenerator = net.oopscraft.application.core.spring.FullBeanNameGenerator.class,
	lazyInit = true,
	includeFilters = @Filter(type=FilterType.ANNOTATION, value={Controller.class,RestController.class,ControllerAdvice.class})
)
public class ApplicationWebContext implements WebMvcConfigurer {
	
	ThymeleafViewResolver viewResolver;
	SpringResourceTemplateResolver templateResolver;
	TemplateEngine templateEngine;
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * Configures JSON message converter.
	 */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
    	MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
    	messageConverter.setObjectMapper(JsonConverter.getObjectMapper());
    	messageConverters.add(messageConverter);
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("language");
		registry.addInterceptor(localeChangeInterceptor);
	}
	
	@Bean
	public SessionLocaleResolver localeResolver() throws Exception {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.ENGLISH);
		return localeResolver;
	}
	
	@Bean
	public MonitorAgent monitorAgent() throws Exception {
		System.out.println("############");
		return MonitorAgent.getInstance();
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
    @DependsOn({"templateEngine"})
    public ViewResolver thymeleafViewResolver() {
        viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
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
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
	
	@Bean
	public CommonsMultipartResolver multipartResolver() throws Exception {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSizePerFile(10485760);	// limits 10MB
		return multipartResolver;
	}


}
