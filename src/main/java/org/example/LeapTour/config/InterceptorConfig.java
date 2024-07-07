package org.example.LeapTour.config;

import org.example.LeapTour.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 * 拦截所有路径, 放行"/screen/**"(大屏展示模块)
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())

                // 拦截所有的路径 用于统计网站流量
                .addPathPatterns("/**")

                // 放行大屏展示模块 避免重复调用
                .excludePathPatterns("/screen/**");
    }
}
