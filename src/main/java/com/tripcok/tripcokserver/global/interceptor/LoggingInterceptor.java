package com.tripcok.tripcokserver.global.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.domain.member.entity.Role;
import com.tripcok.tripcokserver.global.dto.LogDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.Serializable;
import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID = "TRACE_ID";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Logger logger_logDto = LoggerFactory.getLogger(this.getClass());

    private final ObjectMapper objectMapper;

    public LoggingInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper; // 이미 설정된 빈을 사용
    }

    /* traceId 추가 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // traceId 생성 및 설정
        String traceId = UUID.randomUUID().toString();
        response.setHeader(TRACE_ID, traceId);
        request.setAttribute(TRACE_ID, traceId);
        return true;
    }

    /* 로그를 파일로 변환 */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        if (!(request instanceof ContentCachingRequestWrapper) || !(response instanceof ContentCachingResponseWrapper)) {
            logger.warn("Request or Response is not wrapped properly.");
            return;
        }

        ContentCachingRequestWrapper wrappedRequest = (ContentCachingRequestWrapper) request;
        ContentCachingResponseWrapper wrappedResponse = (ContentCachingResponseWrapper) response;

        // 세션에서 memberId 가져오기
        String memberId = null;
        try {
            HttpSession session = request.getSession(false); // 세션이 없으면 null 반환
            if (session != null) {
                JMember member = (JMember) session.getAttribute("member");
                if (member != null) {
                    memberId = String.valueOf(member.getId());
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to retrieve memberId from session.", e);
        }

        // LogDto 생성 및 로깅
        LogDto logDto = new LogDto(wrappedRequest, wrappedResponse);
        logDto.setMemberId(memberId);

        String jsonResult = objectMapper.writeValueAsString(logDto);
        logger_logDto.info(jsonResult);

        // 응답 본문 복사
        wrappedResponse.copyBodyToResponse();
    }

    @Getter
    public static class JMember implements Serializable {
        private static final long serialVersionUID = 1L;
        private Long id;
        private String name;
        private Role role;

        public JMember(Member member) {
            this.id = member.getId();
            this.name = member.getName();
            this.role = member.getRole();
        }
    }
}