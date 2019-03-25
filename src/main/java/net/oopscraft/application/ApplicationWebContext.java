package net.oopscraft.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;

@EnableWebMvc
@ComponentScan(
	basePackages = "net.oopscraft.application",
	nameGenerator = net.oopscraft.application.core.spring.FullBeanNameGenerator.class,
	lazyInit = true,
	includeFilters = @Filter(type=FilterType.ANNOTATION, value={Controller.class,RestController.class, ControllerAdvice.class})
)
public class ApplicationWebContext implements WebMvcConfigurer {
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	/**
	 * Creates tiles view resolver bean.
	 * @return
	 * @throws Exception
	 */
	@Bean
	public UrlBasedViewResolver tilesViewResolver() throws Exception {
		UrlBasedViewResolver tilesViewResolver = new UrlBasedViewResolver();
		tilesViewResolver.setViewClass(org.springframework.web.servlet.view.tiles3.TilesView.class);
		tilesViewResolver.setOrder(1);
		return tilesViewResolver;
	}
	
	/**
	 * Creates TilesConfigurer bean.
	 * @return
	 * @throws Exception
	 */
	@Bean
	public TilesConfigurer tilesConfigurer() throws Exception {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setCheckRefresh(true);
		tilesConfigurer.setDefinitions("classpath:conf/tiles.xml");
		return tilesConfigurer;
	}
	
	/**
	 * Creates JSTL view resolver bean.
	 * @return
	 * @throws Exception
	 */
	@Bean
    public UrlBasedViewResolver jstlViewResolver() throws Exception {
    	UrlBasedViewResolver jstlViewResolver = new UrlBasedViewResolver();
        jstlViewResolver.setViewClass(org.springframework.web.servlet.view.JstlView.class);
        jstlViewResolver.setPrefix("/WEB-INF/view/");
        jstlViewResolver.setOrder(2);
        return jstlViewResolver;
    }
	
	@Bean
	public CommonsMultipartResolver multipartResolver() throws Exception {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSizePerFile(10485760);	// limits 10MB
		return multipartResolver;
	}
    
}
