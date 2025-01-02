package kr.hhplus.be.server.interfaces.dto.payment;

public record OrderProductRequest(
        Long productId,
        Long price,
        Long quantity
) {
}
