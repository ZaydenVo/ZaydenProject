package com.zayden.chat_service.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception.", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_KEY(1004, "Invalid message key!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    PROFILE_NOT_FOUND(1009, "User profile not found!", HttpStatus.BAD_REQUEST),
    CONVERSATION_NOT_FOUND(1001, "Conversation not found!", HttpStatus.NOT_FOUND),
    ;

    int code;
    String message;
    HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
