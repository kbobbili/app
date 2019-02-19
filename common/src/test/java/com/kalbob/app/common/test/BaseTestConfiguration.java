package com.kalbob.app.common.test;

import com.github.javafaker.Faker;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
//@PropertySource("classpath:if-you-need-some-other-file.yml")
//@EnableAutoConfiguration(exclude = XyzConfiguration.class)
//@ComponentScan(excludeFilters={@ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE, value=XyzConfiguration.class)})
public class BaseTestConfiguration {
    @Bean
    public Faker faker() {
        return Faker.instance(Locale.US);
    }

    @Bean
    public DataFactory dataFactory() {
        return new DataFactory();
    }
}