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

        log.info("Pre Handle => " + member.toString());

        if (!member.getRole().equals(Role.MANAGER)) {
            response.sendRedirect("/admin/login");
            return false;
        }

        // 인증 정보가 있으면 요청 계속 처리
        return true;
    }

}
