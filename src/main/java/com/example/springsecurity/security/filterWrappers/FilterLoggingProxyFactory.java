package com.example.springsecurity.security.filterWrappers;

import javax.servlet.Filter;
import java.lang.reflect.Proxy;

public class FilterLoggingProxyFactory {
    public Filter getLoggingWrappedFilter(Filter filter) {
        return (Filter) Proxy.newProxyInstance(
                filter.getClass().getClassLoader(),
                new Class<?>[]{Filter.class},
                new LoggingProxyHandler(filter)
        );
    }
}