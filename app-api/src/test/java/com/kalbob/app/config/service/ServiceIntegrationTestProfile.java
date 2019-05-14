package com.kalbob.app.config.service;

import com.kalbob.app.ApplicationITConfiguration;
import com.kalbob.app.config.IntegrationTestProfile;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DataJpaTest
@Import(ApplicationITConfiguration.class)
@IntegrationTestProfile
public @interface ServiceIntegrationTestProfile {

}
