package com.zayden_project.addFriend_service.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1006, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(1008, "User not found!", HttpStatus.NOT_FOUND),
    FRIEND_REQUEST_NOT_FOUND(1009, "Friend request not found!", HttpStatus.NOT_FOUND),
    INVALID_REQUEST(1009, "Invalid request!", HttpStatus.BAD_REQUEST),
    FRIENDSHIPS_NOT_FOUND(1010, "Friendship not found!", HttpStatus.NOT_FOUND),
    CANNOT_ADD_SELF(1011, "Can not add self!", HttpStatus.BAD_REQUEST),
    CANNOT_ACCEPT_SELF_REQUEST(1012, "Can accept self request!", HttpStatus.BAD_REQUEST),
    PERMISSION_DENIED(1013, "Permission denied!", HttpStatus.BAD_REQUEST),
    CANNOT_REJECT_OWN_REQUEST(1014, "Can not reject own request!", HttpStatus.BAD_REQUEST),
    ALREADY_SENT_REQUEST(1007, "You have already sent a request this person.", HttpStatus.CONFLICT);

    int code;
    String message;
    HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
