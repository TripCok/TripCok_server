package com.tripcok.tripcokserver.global.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripcok.tripcokserver.domain.member.entity.Member;
import com.tripcok.tripcokserver.global.dto.LogDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.UUID;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID = "TRACE_ID";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;
    private final HttpSession httpSession;

    public LoggingInterceptor(ObjectMapper objectMapper, HttpSession httpSession) {
        this.objectMapper = objectMapper;
        this.httpSession = httpSession;
    }

    /* traceId 추가 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //traceId(로그 고유 번호)
        String traceId = UUID.randomUUID().toString();
        response.setHeader(TRACE_ID, traceId);
        return true;
    }

    /* 로그를 파일로 변환 */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

        ContentCachingRequestWrapper wrappedRequest;
        ContentCachingResponseWrapper wrappedResponse;

        // Request와 Response를 캐싱 가능한 래퍼로 감싸기
        if (request instanceof ContentCachingRequestWrapper && response instanceof ContentCachingResponseWrapper) {
            wrappedRequest = (ContentCachingRequestWrapper) request;
            wrappedResponse = (ContentCachingResponseWrapper) response;
            // 이후 로직 실행
        } else {
            return;
        }

        //memberId 추가
        try {
            Member member = (Member) wrappedRequest.getSession().getAttribute("member");
            String memberId = String.valueOf(member.getId());

            wrappedRequest.setAttribute("memberId", memberId);
        } catch (NullPointerException e) {
            return;
        }

        //LogDto 생성
        LogDto logDto = new LogDto(wrappedRequest, wrappedResponse);

        //LogDto를 String으로 저장
        String jsonResult = objectMapper.writeValueAsString(logDto);

        logger.info(jsonResult);
    }

}
