package kr.hhplus.be.server.interfaces.api.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonErrorResponse> handleException(Exception e){
        log.error("unhandled exception", e);
        return ResponseEntity.internalServerError().body(new CommonErrorResponse("정의되지 않은 에러입니다.", "COMMON_ERROR"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonErrorResponse> handleArgumentException(Exception e){
        log.info("bad request", e);
        return ResponseEntity.badRequest().body(new CommonErrorResponse(e.getMessage(), "BAD_REQUEST"));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<CommonErrorResponse> handleStateException(Exception e){
        log.info("bad state", e);
        return ResponseEntity.badRequest().body(new CommonErrorResponse(e.getMessage(), "BAD_STATE"));
    }
}
