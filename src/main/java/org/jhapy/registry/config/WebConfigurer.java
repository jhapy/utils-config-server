/*
 * Copyright 2020-2020 the original author or authors from the JHapy project.
 *
 * This file is part of the JHapy project, see https://www.jhapy.org/ for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jhapy.registry.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.jhapy.commons.config.AppProperties;
import org.jhapy.commons.utils.HasLogger;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements ServletContextInitializer, HasLogger {

  private final Environment env;

  private final AppProperties appProperties;

  public WebConfigurer(Environment env, AppProperties appProperties) {
    this.env = env;
    this.appProperties = appProperties;
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    String loggerPrefix = getLoggerPrefix("onStartup");
    if (env.getActiveProfiles().length != 0) {
      logger().info(loggerPrefix + "Web application configuration, using profiles: {}",
          env.getActiveProfiles());
    }
    logger().info(loggerPrefix + "Web application fully configured");
  }

  @Bean
  public CorsFilter corsFilter() {
    String loggerPrefix = getLoggerPrefix("corsFilter");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = appProperties.getCors();
    if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
      logger().debug(loggerPrefix + "Registering CORS filter");
      source.registerCorsConfiguration("/api/**", config);
      source.registerCorsConfiguration("/management/**", config);
      source.registerCorsConfiguration("/v2/api-docs", config);
      source.registerCorsConfiguration("/swagger-ui.html**", config);
    }
    return new CorsFilter(source);
  }

}
