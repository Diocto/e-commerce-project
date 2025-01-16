package kr.hhplus.be.server.interfaces.api.advice;

public record CommonErrorResponse(
        String message,
        String code
) {
}
