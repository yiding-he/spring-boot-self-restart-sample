package com.hyd.springbootselfrestartsample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class ApplicationMvcConfigurator implements WebMvcConfigurer {

    public static final Set<String> ALLOW_FOR_FIRST_RUN = new HashSet<>(Arrays.asList(
            "first-run.html", "initialize"
    ));

    @Autowired
    private UserConfigService userConfigService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(
                    HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

                String uri = request.getRequestURI();
                boolean firstRunRequest = ALLOW_FOR_FIRST_RUN.stream().anyMatch(uri::endsWith);

                // 系统没有初始化过就只能访问初始化页面
                if (!firstRunRequest && !userConfigService.isInitialized()) {
                    response.sendRedirect("first-run.html");
                    return false;
                }

                // 系统已经初始化过就不能再访问初始化页面
                if (firstRunRequest && userConfigService.isInitialized()) {
                    response.sendRedirect("login.html");
                    return false;
                }

                return true;
            }
        });
        log.info("First-run interceptor initialized.");
    }
}
