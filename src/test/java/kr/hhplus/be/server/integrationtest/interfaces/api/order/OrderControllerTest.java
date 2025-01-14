package kr.hhplus.be.server.integrationtest.interfaces.api.order;

import jakarta.transaction.Transactional;
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

    @BeforeEach
    void setUp() {
        productRepository.save(Product.builder().name("product1").price(1000L).build());
        productRepository.save(Product.builder().name("product2").price(2000L).build());
        productRepository.save(Product.builder().name("product3").price(3000L).build());
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
                        "      \"productId\": 1,\n" +
                        "      \"quantity\": 1\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"userCouponId\": null\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.orderProductList[0].productId").value(1))
                .andExpect(jsonPath("$.orderProductList[0].price").value(1000))
                .andExpect(jsonPath("$.orderProductList[0].quantity").value(1))
                .andExpect(jsonPath("$.totalAmount").value(1000))
                .andExpect(jsonPath("$.discountAmount").value(0));
        // then
    }

}
