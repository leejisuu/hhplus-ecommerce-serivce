package kr.hhplus.be.server.interfaces.api.common;

import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import kr.hhplus.be.server.support.exception.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.getMessage(),
                errorCode.getStatus().value()
        );
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation() {
        ErrorCode errorCode = ErrorCode.ALREADY_ISSUED_COUPON;
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.ALREADY_ISSUED_COUPON.getMessage(),
                errorCode.getStatus().value()
        );
        return new ResponseEntity<>(errorResponse, errorCode.getStatus());
    }
}
