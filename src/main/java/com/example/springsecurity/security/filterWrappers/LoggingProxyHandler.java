package com.example.springsecurity.security.filterWrappers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LoggingProxyHandler implements InvocationHandler {

    private final Filter filter;
    private final Logger logger;

    public LoggingProxyHandler(Filter filter) {
        this.filter = filter;
        logger = LoggerFactory.getLogger(filter.getClass());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("Invoking {}", method.getName());
        logger.info("With arguments {}", args);

        try {
            return method.invoke(filter, args);
        } catch (InvocationTargetException e) {
            logger.error("when invoking {} on {}", method.getName(), filter.getClass().getCanonicalName());

            throw e.getCause();
        }
    }
}
