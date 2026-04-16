/*
 * @ (#) .java    1.0
 * Copyright (c)  IUH. All rights reserved.
 */
package edu.iuh.fit.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
 * @description
 * @author: Huu Thai
 * @date:
 * @version: 1.0
 */
@Configuration
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {

    @Value("${thai.upload-file.base-uri}")
    private String baseURI;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/storage/**")
                .addResourceLocations(baseURI);
    }
}

