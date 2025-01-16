package kr.hhplus.be.server.integrationtest.interfaces.api.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.IBalanceRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.ICouponRepository;
import kr.hhplus.be.server.domain.coupon.IUserCouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.interfaces.api.payment.OrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PaymentControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    IProductRepository productRepository;

    @Autowired
    ICouponRepository couponRepository;

    @Autowired
    IUserCouponRepository userCouponRepository;

    @Autowired
    IBalanceRepository balanceRepository;

    @BeforeEach
    void setUp() {
        /// TODO: 셋업한 아이템의 ID 를 테스트시 전달 가능해야함. 또는 고정 ID 를 부여할것
        productRepository.save(Product.builder().name("product1").price(1000L).stock(10L).build());
        productRepository.save(Product.builder().name("product2").price(2000L).stock(10L).build());
        productRepository.save(Product.builder().name("product3").price(3000L).stock(10L).build());

        Coupon coupon = Coupon.builder().discountPercent(50L).name("testCoupon").build();
        couponRepository.save(coupon);
        UserCoupon userCoupon = UserCoupon.builder().coupon(coupon).userId(1L).build();
        userCouponRepository.save(userCoupon);

        balanceRepository.save(Balance.builder().userId(1L).balance(10000L).build());
        balanceRepository.save(Balance.builder().userId(2L).balance(10000L).build());

    }

    @Test
    void 없는주문서_결제요청시_에러_발생() throws Exception {
        mockMvc.perform(post("/payments/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주문서_유저가_다른경우_에러_발생() throws Exception {
        /// given
        MvcResult mvcResult =
                mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("{\n" +
                            "  \"userId\": 1,\n" +
                            "  \"orderProductRequests\": [\n" +
                            "    {\n" +
                            "      \"id\": 1,\n" +
                            "      \"quantity\": 1\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"userCouponId\": 1\n" +
                            "}")
                ).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        OrderController.Response.Order order = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderController.Response.Order.class);
        Long orderId = order.orderId();
        /// when
        mockMvc.perform(post("/payments")
                .contentType("application/json")
                .content("{\n" +
                        "  \"userId\": 2,\n" +
                        "  \"orderId\": " + orderId.toString() + "\n}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 주문서_결제_성공() throws Exception {
        /// given
        MvcResult mvcResult =
                mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"userId\": 1,\n" +
                                "  \"orderProductRequests\": [\n" +
                                "    {\n" +
                                "      \"id\": 1,\n" +
                                "      \"quantity\": 1\n" +
                                "    }\n" +
                                "  ],\n" +
                                "  \"userCouponId\": 1\n" +
                                "}")
                ).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        OrderController.Response.Order order = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderController.Response.Order.class);
        Long orderId = order.orderId();
        /// when
        mockMvc.perform(post("/payments")
                .contentType("application/json")
                .content("{\n" +
                        "  \"userId\": 1,\n" +
                        "  \"orderId\": " + orderId.toString() + "\n}"))
                .andExpect(status().isOk());
    }
}
