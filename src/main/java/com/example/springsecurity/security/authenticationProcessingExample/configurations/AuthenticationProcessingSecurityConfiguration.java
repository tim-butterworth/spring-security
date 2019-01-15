package com.example.springsecurity.security.authenticationProcessingExample.configurations;

import com.example.springsecurity.security.ConfigurerAdapterOrder;
import com.example.springsecurity.security.LoggingFilter;
import com.example.springsecurity.security.authenticationProcessingExample.AuthenticationProcessingFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

@Order(ConfigurerAdapterOrder.AUTH_PROCESSING)
@Configuration
public class AuthenticationProcessingSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        AuthenticationProcessingFilter authFilter = setupFilter();

        http
                .antMatcher("/authenticationProcessing/**")
                .addFilterBefore(
                        authFilter,
                        RequestHeaderAuthenticationFilter.class
                )
                .addFilterBefore(
                        new RedirectFilter(),
                        RequestHeaderAuthenticationFilter.class
                )
                .addFilterAfter(
                        requestHeaderAuthenticationFilter(),
                        RequestHeaderAuthenticationFilter.class
                )
                .addFilterBefore(
                        new LoggingFilter(this.getClass().getCanonicalName()),
                        AuthenticationProcessingFilter.class
                )
                .authorizeRequests().anyRequest().hasRole("CONTRACTOR")
                .and()
                .csrf().disable()
                .httpBasic().disable();
    }

    private AuthenticationProcessingFilter setupFilter() throws Exception {
        AuthenticationProcessingFilter filter = new AuthenticationProcessingFilter();

        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> response.sendRedirect("/authenticationProcessing/bye"));

        return filter;
    }

    private RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() {
        try {
            RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
            requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager());
            requestHeaderAuthenticationFilter.setPrincipalRequestHeader("REQUIRED_HEADER");

            return requestHeaderAuthenticationFilter;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
