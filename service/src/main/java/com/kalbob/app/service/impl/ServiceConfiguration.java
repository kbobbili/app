package com.kalbob.app.service.impl;

import com.kalbob.app.common.service.BaseServiceCommonConfiguration;
import com.kalbob.app.data.config.DataConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import({DataConfiguration.class, BaseServiceCommonConfiguration.class})
public class ServiceConfiguration {
}
