package com.tripcok.tripcokserver.global.dto;

import lombok.Data;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Arrays;

@Data
public class LogDto {
    String traceId;
    String clientIp;
    String url;
    String method;
    String request;
    String response;
    String statusCode;

    public LogDto(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) {
        this.traceId = requestWrapper.getHeader("traceId");
        this.url = requestWrapper.getRequestURL().toString();
        this.method = requestWrapper.getMethod();
        this.request = Arrays.toString(requestWrapper.getContentAsByteArray());
        this.response = Arrays.toString(responseWrapper.getContentAsByteArray());
        this.statusCode = requestWrapper.getHeader("statusCode");
        this.clientIp = requestWrapper.getRemoteAddr();
    }
}
