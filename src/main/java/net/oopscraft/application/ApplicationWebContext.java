package net.oopscraft.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@ComponentScan(
	basePackages = "net.oopscraft.application",
	nameGenerator = net.oopscraft.application.core.spring.FullBeanNameGenerator.class,
	lazyInit = true,
	includeFilters = @Filter(type=FilterType.ANNOTATION, value=Controller.class)
)
public class ApplicationWebContext {

    @Bean
    public InternalResourceViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        System.err.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        
        
 
        
        
        return viewResolver;
    }
    
}
