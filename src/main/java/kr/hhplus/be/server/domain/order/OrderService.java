package kr.hhplus.be.server.domain.order;


import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import lombok.Builder;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    public static class Command{
        @Builder
        public static class ProductCommand{
            private final Long id;
            private final Integer quantity;

            public ProductCommand(Long id, Integer quantity) {
                this.id = id;
                this.quantity = quantity;
            }
        }

        @Builder
        public static class CreateOrderCommand {
            private final Long userId;
            private final List<ProductCommand> products;
            public CreateOrderCommand(Long userId, List<ProductCommand> products) {
                this.userId = userId;
                this.products = products;
            }
        }
    }
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;

    public OrderService(IOrderRepository orderRepository, IProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(Command.CreateOrderCommand command) {
        /// 요청한 상품 정보를 조회한다.
        List<Product> products = productRepository.getProducts(command.products.stream().map(productCommand -> productCommand.id).toList());

        /// 요청한 상품 정보 중, 존재하지 않는 상품이 있는지 확인한다
        if(products.size() != command.products.size()){
            throw new IllegalArgumentException("Invalid product id");
        }

        List<Pair<Product, Integer>> productQuantityList = command.products.stream()
                .map(productCommand -> {
                    Product product = products.stream()
                            .filter(p -> p.getId().equals(productCommand.id))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Invalid product id"));
                    return Pair.of(product, productCommand.quantity);
                })
                .toList();

        /// TODO: 쿠폰 정보가 있을 경우, 해당 쿠폰을 조회한다.
        /// UserCoupon userCoupon = userCouponRepository.findById(command.userCouponId).orElseThrow(() -> new IllegalArgumentException("Invalid coupon id"));
        UserCoupon userCoupon = null;

        /// 정상적인 쿠폰이라면, Coupon 을 전달한다.
        /// Coupon coupon = userCoupon.isValid() ? userCoupon.getCoupon() : null;
        Coupon coupon = null;

        Order order = Order.create(command.userId, productQuantityList, coupon);
        orderRepository.save(order);

        return order;
    }
}
