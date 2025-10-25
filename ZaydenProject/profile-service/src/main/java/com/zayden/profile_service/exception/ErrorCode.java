package com.zayden.profile_service.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception.", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed!", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002, "Username must be at least {min} characters!", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must be at least {min} characters!", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1004, "Invalid message key!", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User is not existed!", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    PARSE_EXCEPTION(1007, "Parse exception.", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1008, "You do not have permission!", HttpStatus.FORBIDDEN),
    INVALID_DOB(1009, "Your age must be at least {min}!", HttpStatus.BAD_REQUEST),
    PROFILE_NOT_FOUND(1010, "User profile not found!", HttpStatus.BAD_REQUEST),
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
