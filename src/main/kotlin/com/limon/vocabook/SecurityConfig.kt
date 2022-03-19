package com.limon.vocabook

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
@Configuration
class SecurityConfig: WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity?) {
        if(http==null)  return
        http.authorizeRequests()
            .antMatchers("/main", "/css/main.css", "/searchWord/**").permitAll()
    }

    override fun configure(web: WebSecurity?) {
        if(web == null) return
        web.ignoring().antMatchers("/css/**", "/js/**")
    }
}