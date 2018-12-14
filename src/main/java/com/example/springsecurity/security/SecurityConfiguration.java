package com.example.springsecurity.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        FakeCDAS filter = setupFilter();

        http
                .addFilterBefore(
                        filter,
                        BasicAuthenticationFilter.class
                )
                .authorizeRequests()
                .anyRequest()
                .hasRole("CONTRACTOR")
                .and()
                .csrf().disable()
                .httpBasic().disable();
    }

    private FakeCDAS setupFilter() throws Exception {
        FakeCDAS filter = new FakeCDAS();

        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> response.sendRedirect("/bye"));

        return filter;
    }
}
