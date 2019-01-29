package com.example.springsecurity.security.authenticationProcessingExample.configurations;

import com.example.springsecurity.security.ConfigurerAdapterOrder;
import com.example.springsecurity.security.authenticationProcessingExample.AuthenticationProcessingFilter;
import com.example.springsecurity.security.authenticationProcessingExample.RedirectFilter;
import com.example.springsecurity.security.filterWrappers.FilterLoggingProxyFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Configuration
@Order(ConfigurerAdapterOrder.AUTH_PROCESSING)
public class AuthenticationProcessingSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final FilterLoggingProxyFactory filterLoggingProxyFactory = new FilterLoggingProxyFactory();

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/authenticationProcessing/**")
                .csrf().disable()
                .httpBasic().disable()
                .addFilterBefore(
                        filterLoggingProxyFactory.getLoggingWrappedFilter(setupAuthenticationProcessingFilter()),
                        RequestHeaderAuthenticationFilter.class
                )
                .addFilterBefore(
                        filterLoggingProxyFactory.getLoggingWrappedFilter(new RedirectFilter()),
                        RequestHeaderAuthenticationFilter.class
                )
                .addFilterBefore(
                        filterLoggingProxyFactory.getLoggingWrappedFilter(requestHeaderAuthenticationFilter()),
                        RequestHeaderAuthenticationFilter.class
                )
                .authorizeRequests().anyRequest().hasRole("CONTRACTOR");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return new PreAuthenticatedAuthenticationToken(null, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_CONTRACTOR")));
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return authentication == PreAuthenticatedAuthenticationToken.class;
            }
        });
    }

    private AuthenticationProcessingFilter setupAuthenticationProcessingFilter() throws Exception {
        AuthenticationProcessingFilter filter = new AuthenticationProcessingFilter();

        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler((request, response, authentication) -> response.sendRedirect("/authenticationProcessing/bye"));

        return filter;
    }

    private RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() {
        try {
            AuthenticationManager authenticationManager = authenticationManager();

            RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
            requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager);
            requestHeaderAuthenticationFilter.setPrincipalRequestHeader("REQUIRED_HEADER");
            requestHeaderAuthenticationFilter.setAuthenticationDetailsSource(new AuthenticationDetailsSource<HttpServletRequest, PreAuthenticatedAuthenticationToken>() {
                @Override
                public PreAuthenticatedAuthenticationToken buildDetails(HttpServletRequest context) {
                    return new PreAuthenticatedAuthenticationToken(
                            null,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_CONTRACTOR"))
                    );
                }
            });
            requestHeaderAuthenticationFilter.setExceptionIfHeaderMissing(false);

            return requestHeaderAuthenticationFilter;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
