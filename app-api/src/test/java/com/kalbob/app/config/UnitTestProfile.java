package com.kalbob.app.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


//READ this
//https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-detecting-config
//https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-auto-configured-additional-auto-config
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ExtendWith({MockitoExtension.class})
@ActiveProfiles("ut")
public @interface UnitTestProfile {

}
