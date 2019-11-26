package com.kalbob.code.config.service;

import com.kalbob.code.config.UnitTestProfile;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@UnitTestProfile
public @interface ServiceUnitTestProfile {

}
