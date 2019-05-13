package com.kalbob.app.config.data;

import com.kalbob.app.ApplicationTestConfiguration;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@SpringBootConfiguration
@EnableAutoConfiguration
@Import(ApplicationTestConfiguration.class)
public class DataTestConfiguration {
}