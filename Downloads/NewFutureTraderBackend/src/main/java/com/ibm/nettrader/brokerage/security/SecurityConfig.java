/*

 * (c) 2021 IBM Financial Industry Solutions GmbH, All rights reserved.
 */

package com.ibm.nettrader.brokerage.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * The Class SecurityConfig.
 * - @author 03186D744
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private Environment environment;
  
  /**
   * Configure.
   *
   * @param auth the auth
   * @throws Exception the exception
   */
  @Override
 protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable();
  }
 
  /**
   * Cors configuration source.
   *
   * @return the cors configuration source
   */ 
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    //configuration.setAllowedOrigins(Arrays.asList("http://localhost:4100", "https://web-frontend-ftng.ftng-fra04-96611e8cd7d9319821c0bb55792cc9b6-0000.eu-de.containers.appdomain.cloud"));
    configuration.setAllowedOrigins(Arrays.asList(environment.getProperty("localhost.cors.url"), 
        environment.getProperty("cloud.cors.url")));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  } 
}
