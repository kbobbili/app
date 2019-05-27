package com.kalbob.app;

import com.kalbob.app.config.data.BaseRepositoryImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class ApplicationConfiguration {

}
