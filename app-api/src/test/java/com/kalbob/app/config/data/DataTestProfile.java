package com.kalbob.app.config.data;

import com.kalbob.app.ApplicationTestConfiguration;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DataJpaTest
@Import(ApplicationTestConfiguration.class)
@ContextConfiguration(classes = DataTestConfiguration.class)
@UnitTestProfile
@IntegrationTestProfile
public @interface DataTestProfile {

}
