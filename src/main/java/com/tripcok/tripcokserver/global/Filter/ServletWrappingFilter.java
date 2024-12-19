package com.tripcok.tripcokserver.global.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class ServletWrappingFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        /* 요청 진입 시점 기록 */
        requestWrapper.setAttribute("KEY_REQUEST_LOGGER_REQUEST_INCOMING_DATETIME", System.currentTimeMillis());

        // 필터 체인을 진행 (이 시점에서 요청 본문이 캐싱됨)
        filterChain.doFilter(requestWrapper, responseWrapper);

        // 응답 본문을 다시 클라이언트에 전달
        responseWrapper.copyBodyToResponse();
    }
}
