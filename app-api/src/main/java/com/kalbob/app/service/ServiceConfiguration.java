package com.kalbob.app.service;

import com.kalbob.app.data.DataConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import({DataConfiguration.class})
public class ServiceConfiguration {

}
