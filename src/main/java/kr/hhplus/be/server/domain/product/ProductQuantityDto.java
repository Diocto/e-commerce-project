package kr.hhplus.be.server.domain.product;

import java.io.Serializable;

public record ProductQuantityDto (
        Product product,
        Long totalQuantity
    ) implements Serializable {}
