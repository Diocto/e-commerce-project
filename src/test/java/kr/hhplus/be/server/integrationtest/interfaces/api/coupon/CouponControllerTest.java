package kr.hhplus.be.server.integrationtest.interfaces.api.coupon;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.ICouponRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CouponControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ICouponRepository couponRepository;

    /// Test data
    Coupon coupon1;
    Coupon coupon2;

    @BeforeEach
    void setup(){
        coupon1 = couponRepository.save(
                Coupon.builder()
                        .name("텅빈쿠폰")
                        .limitCouponCount(100L)
                        .issuedCouponCount(0L)
                        .discountPercent(50L)
                        .build());
        coupon2 = couponRepository.save(
                Coupon.builder()
                        .name("꽉찬쿠폰")
                        .limitCouponCount(100L)
                        .discountPercent(50L)
                        .issuedCouponCount(100L)
                        .build());
    }

    @Test
    void 쿠폰_발급가능할때_요청하면_성공() throws Exception{
        /// 예외가 발생하면 안됨
        mockMvc.perform(post("/coupons/"+ coupon1.getId() +"/users/1"))
                .andExpect(jsonPath("$.couponCreateRequestStatus").value("success"))
                .andExpect(jsonPath("$.createdUserCouponId").exists());
    }

    @Test
    void 쿠폰_발급만료일때_요청하면_실패한다() throws Exception {
        mockMvc.perform(post("/coupons/"+ coupon2.getId() +"/users/1"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("BAD_STATE"))
                .andExpect(jsonPath("$.message").value("이미 소진된 쿠폰입니다"));
        ;
    }

}
