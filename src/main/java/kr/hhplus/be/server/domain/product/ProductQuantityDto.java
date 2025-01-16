package kr.hhplus.be.server.domain.product;

public record ProductQuantityDto (
        Product product,
        Long totalQuantity
    ) {}
