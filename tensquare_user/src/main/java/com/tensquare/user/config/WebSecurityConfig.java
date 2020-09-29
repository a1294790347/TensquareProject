package com.tensquare.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //authorizeRequests所有security全注解配置实现的开端，表示开始说明需要的权限。
        //需要的权限分两部分，第一部分是拦截的路径，第二部分访问该路径所需要的权限
        //antMatchers表示拦截什么路径，permitAll表示任何权限都可以访问，可以用hasAnyRole或者access来设置权限
        //.and().csrf().disable()固定写法，表示使csrf拦截失效
        http.authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated().and().csrf().disable();
    }
}
