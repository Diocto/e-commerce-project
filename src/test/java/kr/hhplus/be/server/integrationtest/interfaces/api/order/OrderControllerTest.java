package kr.hhplus.be.server.integrationtest.interfaces.api.order;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.ICouponRepository;
import kr.hhplus.be.server.domain.coupon.IUserCouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.infrastructure.product.ProductRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    IProductRepository productRepository;

    @Autowired
    ICouponRepository couponRepository;

    @Autowired
    IUserCouponRepository userCouponRepository;

    Product initProduct1;
    Product initProduct2;
    Product initProduct3;

    Coupon initCoupon;
    UserCoupon initUserCoupon;

    @BeforeEach
    void setUp() {
        initProduct1 = productRepository.save(Product.builder().name("product1").price(1000L).build());
        initProduct2 = productRepository.save(Product.builder().name("product2").price(2000L).build());
        initProduct3 = productRepository.save(Product.builder().name("product3").price(3000L).build());

        initCoupon = Coupon.builder().discountPercent(50L).name("testCoupon").build();
        couponRepository.save(initCoupon);
        initUserCoupon = UserCoupon.builder().coupon(initCoupon).userId(1L).build();
        userCouponRepository.save(initUserCoupon);
    }

    @Test
    void 상품_한개_주문서_요청시_주문서정보를_반환한다() throws Exception {
        // given

        // when
        // Request.OrderBody 에 맞는 json body 형식으로 요청
        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"userId\": 1,\n" +
                                "  \"orderProductRequests\": [\n" +
                                "    {\n" +
                                "      \"productId\": "+initProduct1.getId()+" ,\n" +
                                "      \"quantity\": 1\n" +
                                "    }\n" +
                                "  ],\n" +
                                "  \"userCouponId\": null\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.orderProductList[0].productId").value(initProduct1.getId()))
                .andExpect(jsonPath("$.orderProductList[0].price").value(1000))
                .andExpect(jsonPath("$.orderProductList[0].quantity").value(1))
                .andExpect(jsonPath("$.totalAmount").value(1000))
                .andExpect(jsonPath("$.discountAmount").value(0));
        // then
    }

    @Test
    void 상품_한개_쿠폰과_함께_주문시_할인된_주문서정보를_반환한다() throws Exception {
        // given

        // when
        // Request.OrderBody 에 맞는 json body 형식으로 요청
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
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.orderProductList[0].productId").value(initProduct1.getId()))
                .andExpect(jsonPath("$.orderProductList[0].price").value(1000))
                .andExpect(jsonPath("$.orderProductList[0].quantity").value(1))
                .andExpect(jsonPath("$.totalAmount").value(500))
                .andExpect(jsonPath("$.discountAmount").value(500))
                .andExpect(jsonPath("$.userCouponId").value(initUserCoupon.getId()));
        // then
    }

    @Test
    void 상품_여러개_쿠폰과_함께_주문시_할인된_주문서정보를_반환한다() throws Exception {
        // given

        // when
        // Request.OrderBody 에 맞는 json body 형식으로 요청
        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"userId\": 1,\n" +
                                "  \"orderProductRequests\": [\n" +
                                "    {\n" +
                                "      \"productId\": "+initProduct1.getId()+",\n" +
                                "      \"quantity\": 1\n" +
                                "    },\n" +
                                "    {\n" +
                                "      \"productId\": "+initProduct2.getId()+",\n" +
                                "      \"quantity\": 1\n" +
                                "    }\n" +
                                "  ],\n" +
                                "  \"userCouponId\": "+ initUserCoupon.getId()+"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.orderProductList[0].productId").value(initProduct1.getId()))
                .andExpect(jsonPath("$.orderProductList[0].price").value(1000))
                .andExpect(jsonPath("$.orderProductList[0].quantity").value(1))
                .andExpect(jsonPath("$.orderProductList[1].productId").value(initProduct2.getId()))
                .andExpect(jsonPath("$.orderProductList[1].price").value(2000))
                .andExpect(jsonPath("$.orderProductList[1].quantity").value(1))
                .andExpect(jsonPath("$.totalAmount").value(1500))
                .andExpect(jsonPath("$.discountAmount").value(1500))
                .andExpect(jsonPath("$.userCouponId").value(initUserCoupon.getId()));
        // then
    }

    @Test
    void 없는_상품_주문서_요청시_400_에러를_반환한다() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("{\n" +
                                "  \"userId\": 1,\n" +
                                "  \"orderProductRequests\": [\n" +
                                "    {\n" +
                                "      \"productId\": 939393939230,\n" +
                                "      \"quantity\": 1\n" +
                                "    }\n" +
                                "  ],\n" +
                                "  \"userCouponId\": null\n" +
                                "}"))
                .andExpect(status().is4xxClientError());
        // then
    }
}
