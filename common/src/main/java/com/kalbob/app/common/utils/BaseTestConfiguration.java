package com.kalbob.app.common.utils;

import com.github.javafaker.Faker;
import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
//@PropertySource("classpath:if-you-need-some-other-file.yml")
//Below annotations won't work because this configuration is not in test scope
//@TestPropertySource(locations = {"classpath:if-you-need-some-other-file.yml"})
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