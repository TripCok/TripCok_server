package com.tripcok.tripcokserver.domain.post.service;

public class UnauthorizedAccessException extends Throwable {
        // Default constructor
    public UnauthorizedAccessException() {
            super("권한이 없습니다."); // 기본 메시지
        }

        // Constructor with custom message
    public UnauthorizedAccessException(String message) {
            super(message);
        }
}

