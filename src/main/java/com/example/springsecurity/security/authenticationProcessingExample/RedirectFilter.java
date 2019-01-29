package com.example.springsecurity.security.authenticationProcessingExample;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class RedirectFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain chain) {
        Optional<String> maybeLetMeIn = Optional.ofNullable(servletRequest.getHeader("LET_ME_IN"));
        Optional<HttpSession> maybeSession = Optional.ofNullable(servletRequest.getSession());

        Consumer<Object> successHandler = (s) -> {
            try {
                chain.doFilter(servletRequest, servletResponse);
            } catch (IOException | ServletException e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        };

        maybeLetMeIn.ifPresent(successHandler);
        maybeSession.ifPresent(successHandler);

        if(!maybeLetMeIn.isPresent() && !maybeSession.isPresent()) {
            servletResponse.setStatus(302);
            servletResponse.setHeader("Location", "https://google.com");
        }
    }

}
