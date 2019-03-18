package com.zjh.crosdemo.config;

import com.zjh.crosdemo.config.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new CorsFilter());
        bean.addUrlPatterns("/*");
        bean.setName("LogCostFilter");
        bean.setOrder(1);
        return bean;
    }
}
