package com.kalbob.app.common.utils;

import org.springframework.context.annotation.Configuration;

@Configuration
//@PropertySource("classpath:if-you-need-some-other-file.yml")
//Below annotations won't work because this configuration is not in test scope
//@TestPropertySource(locations = {"classpath:if-you-need-some-other-file.yml"})
public class BaseDataTestConfiguration {

}