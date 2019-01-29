package com.example.springsecurity.security.autowiredFilter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
public class AutowiredSabatogeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("This is an autowired filter!");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
