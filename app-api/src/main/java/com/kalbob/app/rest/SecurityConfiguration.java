package com.kalbob.app.rest;

import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Order(100)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

    }

}

@Order(90)
@Profile({"dev"})
@EnableWebSecurity
class SecurityConfigurationDev extends WebSecurityConfigurerAdapter {

    @Override
    @Profile({"dev"})
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers().frameOptions().disable().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .and()
                .authorizeRequests().antMatchers("/h2-console/**").permitAll();//For h2, disable CSRF, X-Frame-Options
    }

}