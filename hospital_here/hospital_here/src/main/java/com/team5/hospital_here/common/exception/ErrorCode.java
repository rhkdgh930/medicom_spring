package com.team5.hospital_here.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    USER_ALREADY_EXISTS(409, "USER_ALREADY_EXISTS", "사용자가 이미 존재함"),
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "사용자를 찾을 수 없음"),
    NO_PERMISSION(400,"NO_PERMISSION","권한없음"),
    INVALID_USER_CREDENTIALS(401, "INVALID_USER_CREDENTIALS", "비밀번호 틀림")

    ;

    public int code;
    public String codeName;
    public String message;

    ErrorCode(int code, String codeName, String message) {
        this.code = code;
        this.codeName = codeName;
        this.message = message;
    }
}
