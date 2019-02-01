package net.oopscraft.application.api;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration extends WebMvcConfigurationSupport {

	@Bean
	public Docket swaggerDoclet(){
		return new Docket(DocumentationType.SWAGGER_2)
			.apiInfo(getApiInfo())	  
			.select()
			.apis(RequestHandlerSelectors.basePackage(this.getClass().getPackage().getName()))
			.paths(PathSelectors.any())
			.build();
	}

	@SuppressWarnings("rawtypes")
	private ApiInfo getApiInfo() {
		ApiInfo apiInfo = new ApiInfo(
			"OOPSCRAFT-APPLICATION API",		// title
			null,							// description
			"SNAPSHOT", 						// version
			null,							// termsOfServiceUrl	
			null, 							// contact
			null, 							// license
			null, 							// licenseUrl
			new ArrayList<VendorExtension>());
	   return apiInfo;
   }
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/swagger-ui.html");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
