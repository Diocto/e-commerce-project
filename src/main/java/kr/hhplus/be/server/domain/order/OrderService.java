package kr.hhplus.be.server.domain.order;


import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.IUserCouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    public static class Command{
        @Builder
        public static class ProductCommand{
            private final Long id;
            private final Long quantity;

            public ProductCommand(Long id, Long quantity) {
                this.id = id;
                this.quantity = quantity;
            }
        }

        @Builder
        @AllArgsConstructor
        public static class CreateOrderCommand {
            private final Long userId;
            private final List<ProductCommand> products;
            private final Coupon coupon;
            private final UserCoupon userCoupon;

        }
    }
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;

    public OrderService(IOrderRepository orderRepository, IProductRepository productRepository, IUserCouponRepository userCouponRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(Command.CreateOrderCommand command) {
        /// 요청한 상품 정보를 조회한다.
        List<Product> products = productRepository.getProducts(command.products.stream().map(productCommand -> productCommand.id).toList());

        /// 요청한 상품 정보 중, 존재하지 않는 상품이 있는지 확인한다 -> Product 도메인으로 이동해야 할듯?
        if(products.size() != command.products.size()){
            throw new IllegalArgumentException("Invalid product id");
        }

        List<Pair<Product, Long>> productQuantityList = command.products.stream()
                .map(productCommand -> {
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(productCommand.id))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));
                    return Pair.of(product, productCommand.quantity);
                })
                .toList();

        Order order = Order.create(command.userId, productQuantityList, command.coupon, command.userCoupon);
        orderRepository.save(order);

        return order;
    }
}
