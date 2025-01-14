package kr.hhplus.be.server.domain.payment;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.balance.IBalanceRepository;
import kr.hhplus.be.server.domain.order.IOrderRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.infrastructure.product.ProductRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {

    private final IProductRepository productRepository;
    private final IBalanceRepository balanceRepository;
    private final IOrderRepository orderRepository;

    public PaymentService(ProductService productService, OrderService orderService, BalanceService balanceService, ProductRepositoryImpl productRepositoryImpl, IProductRepository productRepository, IBalanceRepository balanceRepository, IOrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.balanceRepository = balanceRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void payOrder(Long orderId, Long userId) {
        /// 이 함수가 도메인 엔티티를 받지 않는 이유는, 트랙잭션 경계를 명확하게 하기 위함임.
        /// MSA 로 확장을 하는 것에 대한 고려를 한다면 서비스를 호출해야 마땅한가?
        /// 하지만 여기는 모놀리식 이기 때문에 이정도로만 해도 나중에 확장하기 편할 것이라는 기대.

        Balance balance = balanceRepository.findByIdWithLock(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저에게 할당된 잔액이 없습니다"));
        Order order = orderRepository.findByIdWithLock(orderId).orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다"));

        Map<Long, Product> productMap = productRepository.getProductsWithLock(order.getOrderProducts().stream().map(OrderProduct::getProductId).toList());
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = productMap.get(orderProduct.getProductId());
            product.decreaseStock(orderProduct.getQuantity());
        }

        balance.use(order.getAmount());
        order.complete();

        orderRepository.save(order);
        balanceRepository.save(balance);
        productRepository.saveAll(productMap.values().stream().toList());
    }
}
