package com.tripcok.tripcokserver.global.interceptor;

import com.tripcok.tripcokserver.domain.member.entity.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info("AdminInterceptor preHandle => " + request.getSession().getAttribute("member"));

        LoggingInterceptor.JMember member = (LoggingInterceptor.JMember) request.getSession().getAttribute("member");

        try {
            if (member.getRole().equals(Role.MANAGER)) {
                return true;
            }
        } catch (NullPointerException e) {
            response.sendRedirect("/admin/login");
            return false;
        }

        return true;
    }

}
