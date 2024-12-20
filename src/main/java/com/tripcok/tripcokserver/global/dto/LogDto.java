package com.tripcok.tripcokserver.global.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;


@Data
public class LogDto {

    String traceId;
    String memberId;
    String clientIp;
    String url;
    String method;
    String request;
    String response;
    String statusCode;
    LocalDateTime requestTime;
    Long time;

    @JsonIgnore
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LogDto(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) throws IOException {
        this.traceId = responseWrapper.getHeader("TRACE_ID");
        this.url = requestWrapper.getRequestURL().toString();
        this.method = requestWrapper.getMethod();
        this.request = new String(requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        this.response = new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        this.statusCode = String.valueOf(responseWrapper.getStatus());
        this.clientIp = requestWrapper.getRemoteAddr();
        this.requestTime = LocalDateTime.now();
        this.time = System.currentTimeMillis();
        this.memberId = (String) requestWrapper.getAttribute("memberId");
    }

}
