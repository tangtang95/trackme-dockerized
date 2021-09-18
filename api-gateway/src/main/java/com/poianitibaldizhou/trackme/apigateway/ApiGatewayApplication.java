package com.poianitibaldizhou.trackme.apigateway;

import com.poianitibaldizhou.trackme.apigateway.filter.post.HrefFilter;
import com.poianitibaldizhou.trackme.apigateway.filter.pre.AccessControlFilter;
import com.poianitibaldizhou.trackme.apigateway.filter.route.TranslationFilter;
import com.poianitibaldizhou.trackme.apigateway.security.TokenAuthenticationFilter;
import com.poianitibaldizhou.trackme.apigateway.service.ThirdPartyAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.service.UserAuthenticationService;
import com.poianitibaldizhou.trackme.apigateway.util.ApiUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class ApiGatewayApplication {


	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public AccessControlFilter accessControlFilter(UserAuthenticationService userAuthenticationService,
												   ThirdPartyAuthenticationService thirdPartyAuthenticationService,
												   TokenAuthenticationFilter tokenAuthenticationFilter,
												   ApiUtils apiUtils) {
		return new AccessControlFilter(userAuthenticationService, thirdPartyAuthenticationService, apiUtils, tokenAuthenticationFilter);
	}

	@Bean
	public TranslationFilter translationFilter() {
		return new TranslationFilter();
	}

	@Bean
	public HrefFilter hrefFilter() { return new HrefFilter(); }
}
