package kr.hhplus.be.server.integrationtest.interfaces.api.coupon;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.coupon.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CouponControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ICouponRepository couponRepository;

    @Autowired
    IUserCouponRepository userCouponRepository;

    @Autowired
    CouponScheduler couponScheduler;

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

    @Test
    void 쿠폰_동시에_100명의_유저가_쿠폰발급을_신청하면_100개까지만_성공한다() throws Exception {
        IntStream.range(0, 100).parallel().forEach(i -> {
            try {
                mockMvc.perform(post("/coupons/"+ coupon1.getId() +"/users/"+ i))
                        .andExpect(jsonPath("$.couponCreateRequestStatus").value("success"))
                        .andExpect(jsonPath("$.createdUserCouponId").exists());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        mockMvc.perform(post("/coupons/"+ coupon1.getId() +"/users/101"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("BAD_STATE"))
                .andExpect(jsonPath("$.message").value("이미 소진된 쿠폰입니다"));
    }

    @Test
    void 한정판쿠폰_v2_동시애_100명의_유저가_쿠폰발급을_신청하면_100개까지만_성공한다() throws Exception {
        IntStream.range(0, 100).parallel().forEach(i -> {
            try {
                mockMvc.perform(post("/v2/coupons/"+ coupon1.getId() +"/users/"+ i))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /// 쿠폰 발급 처리
        couponScheduler.processLimitedCouponRequest();

        /// 100개의 쿠폰이 100명에게 발급되었는지 repository 를 이용하여 for문을 돌면서 확인
        LongStream.range(0, 100).forEach(i -> {
            UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(i, coupon1.getId()).orElseThrow();
        });

        /// 101번째 유저는 쿠폰이 발급되면 안됨
        mockMvc.perform(post("/v2/coupons/"+ coupon1.getId() +"/users/101"))
                .andExpect(status().isOk());
        couponScheduler.processLimitedCouponRequest();
        assertThrows(IllegalArgumentException.class, () -> userCouponRepository.findByUserIdAndCouponId(101L, coupon1.getId()).orElseThrow());
    }

    @Test
    void 한정판쿠폰_v2_동시애_200명의_유저가_쿠폰발급을_신청하면_순서대로만_성공한다() throws Exception {
        IntStream.range(0, 200).parallel().forEach(i -> {
            try {
                mockMvc.perform(post("/v2/coupons/"+ coupon1.getId() +"/users/"+ i))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /// 쿠폰 발급 처리
        couponScheduler.processLimitedCouponRequest();

        LongStream.range(0, 100).forEach(i -> {
            UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(i, coupon1.getId()).orElseThrow();
        });

        LongStream.range(100, 200).forEach(i -> {
            assertThrows(Exception.class, () -> userCouponRepository.findByUserIdAndCouponId(i, coupon1.getId()).orElseThrow());
        });


    }
}
