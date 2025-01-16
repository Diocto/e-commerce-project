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

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    Product initProduct1;
    Product initProduct2;
    Product initProduct3;

    Coupon initCoupon;
    UserCoupon initUserCoupon;

    Balance initBalance1;
    Balance initBalance2;

    @BeforeEach
    void setUp() {
        /// TODO: 셋업한 아이템의 ID 를 테스트시 전달 가능해야함. 또는 고정 ID 를 부여할것
        initProduct1 = productRepository.save(Product.builder().name("product1").price(1000L).stock(10L).build());
        initProduct2 = productRepository.save(Product.builder().name("product2").price(2000L).stock(10L).build());
        initProduct3 = productRepository.save(Product.builder().name("product3").price(3000L).stock(10L).build());

        initCoupon = Coupon.builder().discountPercent(50L).name("testCoupon").build();
        couponRepository.save(initCoupon);
        initUserCoupon = UserCoupon.builder().coupon(initCoupon).userId(1L).build();
        userCouponRepository.save(initUserCoupon);

        initBalance1 = balanceRepository.save(Balance.builder().userId(1L).balance(10000L).build());
        initBalance2 = balanceRepository.save(Balance.builder().userId(2L).balance(10000L).build());


    }

    @Test
    void 없는주문서_결제요청시_에러_발생() throws Exception {
        mockMvc.perform(post("/payments/1"))
                .andExpect(status().isNotFound());
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
                            "      \"productId\": "+initProduct1.getId()+",\n" +
                            "      \"quantity\": 1\n" +
                            "    }\n" +
                            "  ],\n" +
                            "  \"userCouponId\": "+initUserCoupon.getId()+"\n" +
                            "}")
                ).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        OrderController.Response.Order order = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderController.Response.Order.class);
        Long orderId = order.orderId();
        /// when
        assertThrows(Exception.class, () -> {
            mockMvc.perform(post("/payments")
                            .contentType("application/json")
                            .content("{\n" +
                                    "  \"userId\": 2,\n" +
                                    "  \"orderId\": " + orderId.toString() + "\n}"))
                    .andExpect(status().isBadRequest());
        });

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
                                "      \"productId\": "+initProduct1.getId()+",\n" +
                                "      \"quantity\": 1\n" +
                                "    }\n" +
                                "  ],\n" +
                                "  \"userCouponId\": "+initUserCoupon.getId()+"\n" +
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
