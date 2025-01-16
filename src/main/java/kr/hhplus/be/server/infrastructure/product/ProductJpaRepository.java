package kr.hhplus.be.server.infrastructure.product;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductQuantityDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :ids")
    List<Product> findAllByIdWithLock(List<Long> ids);

    /// Order 가 paid 인 OrderProduct 들의 개수를 합해서, 가장 개수가 높은 순서대로 Product 객체를 내려준다.
    @Query(value="""
         SELECT new kr.hhplus.be.server.domain.product.ProductQuantityDto(p, SUM(op.quantity))
         FROM OrderProduct op
         JOIN op.order o
         JOIN op.product p
         WHERE o.status = kr.hhplus.be.server.domain.order.Order.OrderStatus.PAID
         GROUP BY p.id
         ORDER BY SUM(op.quantity) DESC
         """,
         countQuery = """
         SELECT COUNT(DISTINCT p.id)
         FROM OrderProduct op
         JOIN op.order o
         JOIN op.product p
         WHERE o.status = 'PAID'
         """)
    List<ProductQuantityDto> getPopularProducts(Pageable pageRequest);
}
