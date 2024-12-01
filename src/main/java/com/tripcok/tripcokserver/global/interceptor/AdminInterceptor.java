package com.tripcok.tripcokserver.global.interceptor;

import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("AdminInterceptor preHandle => " + request.getSession().getAttribute("member"));

        Member member = (Member) request.getSession().getAttribute("member");

//        log.info("Pre Handle => " + member.toString());

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