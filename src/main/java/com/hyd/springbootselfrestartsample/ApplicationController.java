package com.hyd.springbootselfrestartsample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Controller
@Slf4j
public class ApplicationController {

    @Autowired
    private UserConfigService userConfigService;

    @GetMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        if (!userConfigService.isInitialized()) {
            response.sendRedirect("first-run.html");
        } else {
            response.sendRedirect("login.html");
        }
    }

    @PostMapping("/initialize")
    public void initialize(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (userConfigService.isInitialized()) {
            log.info("系统已经初始化过了，进入登录页面");
            response.sendRedirect("login.html");
            return;
        }

        userConfigService.setConfig("admin-password", request.getParameter("admin-password"));
        userConfigService.setConfig("initialized", "true");
        response.sendRedirect("first-run-success.html");

        log.info("初始化设置完毕，开始重启");
        SpringBootSelfRestartSampleApplication.restart();
    }

    @PostMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String adminPassword = userConfigService.getConfig("admin-password");
        String requestPassword = request.getParameter("admin-password");

        if (Objects.equals(adminPassword, requestPassword)) {
            log.info("登录成功");
            response.sendRedirect("main.html");
        } else {
            log.info("登录失败");
            response.sendRedirect("login.html");
        }
    }
}
