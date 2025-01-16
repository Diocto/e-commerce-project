package kr.hhplus.be.server.integrationtest.interfaces.api.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.ICouponRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ICouponRepository couponRepository;

    @BeforeEach
    void setup(){
        couponRepository.save(
                Coupon.builder()
                        .name("텅빈쿠폰")
                        .limitCouponCount(100L)
                        .discountPercent(50L)
                        .build());
        couponRepository.save(
                Coupon.builder()
                        .name("꽉찬쿠폰")
                        .limitCouponCount(100L)
                        .discountPercent(50L)
                        .issuedCouponCount(100L)
                        .build());
    }

    @Test
    void 쿠폰_발급가능할때_요청하면_성공() throws Exception {
        mockMvc.perform(post("/coupons/1/users/1"))
                .andExpect(jsonPath("$.couponCreateRequestStatus").value("success"))
                .andExpect(jsonPath("$.createdUserCouponId").exists());
    }

    @Test
    void 쿠폰_발급만료일때_요청하면_실패한다(){

    }

}
