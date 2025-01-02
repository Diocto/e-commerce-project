package kr.hhplus.be.server.interfaces.dto.product;

public record ProductViewResponseBody(
        Long productId,
        String productName,
        Long price,
        String description,
        Long stockQuantity
){
}
