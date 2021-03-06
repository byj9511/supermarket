package com.byy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author roger yang
 * @date 11/21/2019
 */
//@Configuration
@ConditionalOnBean(GlobalCorsProperties.class)
public class MarketCorsConfiguration {
    @Autowired
    private  GlobalCorsProperties globalCorsProperties;

    @Bean
    public CorsWebFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        globalCorsProperties.getCorsConfigurations().forEach((path,corsConfiguration)->{
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
            corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
            ArrayList<String> allowedOrigins = new ArrayList<>();
            allowedOrigins.add("*");
            corsConfiguration.setAllowedOrigins(allowedOrigins);
            source.registerCorsConfiguration(path, corsConfiguration);
        });
        return new CorsWebFilter(source);
    }
}
