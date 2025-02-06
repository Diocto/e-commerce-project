package kr.hhplus.be.server.integrationtest.interfaces.api.product;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {
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
        initProduct2 = productRepository.save(Product.builder().name("product2").price(2000L).stock(20L).build());
        initProduct3 = productRepository.save(Product.builder().name("product3").price(3000L).stock(30L).build());

        initCoupon = Coupon.builder().discountPercent(50L).limitCouponCount(10L).name("testCoupon").build();
        couponRepository.save(initCoupon);
        initUserCoupon = UserCoupon.builder().coupon(initCoupon).userId(1L).build();
        userCouponRepository.save(initUserCoupon);

        initBalance1 = balanceRepository.save(Balance.builder().userId(1L).balance(100000000L).build());
        initBalance2 = balanceRepository.save(Balance.builder().userId(2L).balance(10000L).build());

    }

    @Test
    void 인기상품_test_주문목록이_없을시_상품을_id_순서대로_준다() throws Exception {
        mockMvc.perform(get("/products/popular")
                        .queryParam("page", "0")
                        .queryParam("size", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productViewList[0].id").value(initProduct1.getId()))
                .andExpect(jsonPath("$.productViewList[0].productName").value("product1"))
                .andExpect(jsonPath("$.productViewList[0].price").value(1000))
                .andExpect(jsonPath("$.productViewList[0].stockQuantity").value(10))
                .andExpect(jsonPath("$.productViewList[1].id").value(initProduct2.getId()))
                .andExpect(jsonPath("$.productViewList[1].productName").value("product2"))
                .andExpect(jsonPath("$.productViewList[1].price").value(2000))
                .andExpect(jsonPath("$.productViewList[1].stockQuantity").value(20))
                .andExpect(jsonPath("$.productViewList[2].id").value(initProduct3.getId()))
                .andExpect(jsonPath("$.productViewList[2].productName").value("product3"))
                .andExpect(jsonPath("$.productViewList[2].price").value(3000))
                .andExpect(jsonPath("$.productViewList[2].stockQuantity").value(30))
        ;
    }

    @Test
    void 인기상품_test_주문이_있을시_주문개수가_많은순서대로_리스트를준다() throws Exception {
        /// given
        MvcResult mvcResult =
                mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"userId\": 1,\n" +
                                "  \"orderProductRequests\": [\n" +
                                "    {\n" +
                                "      \"productId\": "+initProduct1.getId()+",\n" +
                                "      \"quantity\": 3\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"productId\": "+initProduct2.getId()+",\n" +
                                "      \"quantity\": 10\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"productId\": "+initProduct3.getId()+",\n" +
                                "      \"quantity\": 5\n" +
                                "    }\n" +
                                "  ],\n" +
                                "  \"userCouponId\": "+initUserCoupon.getId()+"\n" +
                                "}")
                ).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        OrderController.Response.Order order = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderController.Response.Order.class);
        Long orderId = order.orderId();

        mockMvc.perform(post("/payments")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"userId\": 1,\n" +
                                "  \"orderId\": " + orderId.toString() + "\n}"))
                .andExpect(status().isOk());

        /// when
        mockMvc.perform(get("/products/popular")
                        .queryParam("page", "0")
                        .queryParam("size", "3"))
                /// then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productViewList[0].id").value(initProduct2.getId()))
                .andExpect(jsonPath("$.productViewList[0].productName").value("product2"))
                .andExpect(jsonPath("$.productViewList[0].price").value(2000))
                .andExpect(jsonPath("$.productViewList[0].stockQuantity").value(10))
                .andExpect(jsonPath("$.productViewList[1].id").value(initProduct3.getId()))
                .andExpect(jsonPath("$.productViewList[1].productName").value("product3"))
                .andExpect(jsonPath("$.productViewList[1].price").value(3000))
                .andExpect(jsonPath("$.productViewList[1].stockQuantity").value(25))
                .andExpect(jsonPath("$.productViewList[2].id").value(initProduct1.getId()))
                .andExpect(jsonPath("$.productViewList[2].productName").value("product1"))
                .andExpect(jsonPath("$.productViewList[2].price").value(1000))
                .andExpect(jsonPath("$.productViewList[2].stockQuantity").value(7))
        ;

        mockMvc.perform(get("/products/popular")
                        .queryParam("page", "0")
                        .queryParam("size", "3"))
                /// then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productViewList[0].id").value(initProduct2.getId()))
                .andExpect(jsonPath("$.productViewList[0].productName").value("product2"))
                .andExpect(jsonPath("$.productViewList[0].price").value(2000))
                .andExpect(jsonPath("$.productViewList[0].stockQuantity").value(10))
                .andExpect(jsonPath("$.productViewList[1].id").value(initProduct3.getId()))
                .andExpect(jsonPath("$.productViewList[1].productName").value("product3"))
                .andExpect(jsonPath("$.productViewList[1].price").value(3000))
                .andExpect(jsonPath("$.productViewList[1].stockQuantity").value(25))
                .andExpect(jsonPath("$.productViewList[2].id").value(initProduct1.getId()))
                .andExpect(jsonPath("$.productViewList[2].productName").value("product1"))
                .andExpect(jsonPath("$.productViewList[2].price").value(1000))
                .andExpect(jsonPath("$.productViewList[2].stockQuantity").value(7))
        ;
    }



}
