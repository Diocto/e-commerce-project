package kr.hhplus.be.server.domain.order;


import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.IUserCouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {public static class Command{
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
    private final IOrderProductRepository orderProductRepository;

    public OrderService(IOrderRepository orderRepository, IProductRepository productRepository, IUserCouponRepository userCouponRepository, IOrderProductRepository orderProductRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderProductRepository = orderProductRepository;
    }

    public Order createOrder(Command.CreateOrderCommand command) {
        /// TODO: Product 도메인으로 이동시켜 줄 것.
        List<Product> products = productRepository.getProducts(command.products.stream().map(productCommand -> productCommand.id).toList());

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

        Order order = Order.create(command.userId, productQuantityList, command.userCoupon);
        order.getOrderProducts().forEach(orderProductRepository::save);
        orderRepository.save(order);

        return order;
    }

    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Invalid order id"));
    }

    public void completeOrder(Order order) {
        order.complete();
        orderRepository.save(order);
    }

}
