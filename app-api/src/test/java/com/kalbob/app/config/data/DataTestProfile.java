package com.kalbob.app.config.data;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DataJpaTest
//@AutoConfigureTestDatabase(replace= Replace.NONE)
@ContextConfiguration(classes = DataTestConfiguration.class)
public @interface DataTestProfile {

}
