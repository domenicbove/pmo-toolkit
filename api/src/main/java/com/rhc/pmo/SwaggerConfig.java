package com.rhc.pmo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket apiConfig() {
		String title = "PMO Toolkit REST service";
		String description = "API documentation of the RESTful service endpoints provided by PMO Toolkit REST service";
		String version = "0.1";
		String termsOfServiceUrl = null;
		String contact = "rfagin@redhat.com";
		String license = null;
		String licenseUrl = null;
		ApiInfo apiInfo = new ApiInfo(title, description, version, termsOfServiceUrl, contact, license, licenseUrl);
		
		return new Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("com.rhc.pmo"))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(apiInfo)
			.genericModelSubstitutes(ResponseEntity.class);
	}
}
