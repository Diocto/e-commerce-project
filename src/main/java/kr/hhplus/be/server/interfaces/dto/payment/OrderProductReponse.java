package kr.hhplus.be.server.interfaces.dto.payment;

public record OrderProductReponse(
        Long productId,
        Long price,
        Long quantity
) {
}
