package com.example.springsecurity.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LoggingFilter implements Filter {

    private final String id;

    public LoggingFilter(String id) {

        this.id = id;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        System.out.println(id + " -> " + httpRequest.getRequestURI());

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
