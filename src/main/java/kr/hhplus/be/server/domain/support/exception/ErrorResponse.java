package kr.hhplus.be.server.domain.support.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String message;
    private int status;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
}