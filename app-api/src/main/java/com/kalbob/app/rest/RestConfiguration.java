package com.kalbob.app.rest;

import com.kalbob.app.service.ServiceConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import({ServiceConfiguration.class})
public class RestConfiguration {

}
