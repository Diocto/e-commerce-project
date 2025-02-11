package kr.hhplus.be.server.domain.coupon;

import org.redisson.api.RKeys;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class CouponScheduler {

    private final RedissonClient redissonClient;
    private final CouponService couponService;
    private static final Long maxCouponRequestProcessCount = 200L;

    public CouponScheduler(RedissonClient redissonClient, CouponService couponService) {
        this.redissonClient = redissonClient;
        this.couponService = couponService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void processLimitedCouponRequest() {
        // 모든 쿠폰 요청 set 에서  가져와, 순차적으로 처리
        RKeys keys = redissonClient.getKeys();
        Iterable<String> couponKeys = keys.getKeysByPattern("coupon-request:*");

        for (String couponKey : couponKeys) {
            // 해당 Ordered Set 에서 가장 먼저 들어온 요청을 가져옴
            RScoredSortedSet<String> couponSet = redissonClient.getScoredSortedSet(couponKey);
            String[] parts = couponKey.split(":");
            if (parts.length != 2) {
                continue;  // 잘못된 키는 건너뜀
            }
            String couponId = parts[1];
            Long processCount = 0L;
            while(couponSet.size() > 0 && processCount < maxCouponRequestProcessCount) {
                String userId = couponSet.pollFirst();
                if (userId == null) {
                    break;  // 더 이상 요청이 없으면 루프 종료
                }
                // 쿠폰 생성
                try {
                    couponService.createLimitedCoupon(Long.parseLong(userId), Long.parseLong(couponId));
                    System.out.println("쿠폰 생성 성공 : " + userId);
                } catch (Exception e) {
                    // 쿠폰 생성 실패시 다시 요청
                    System.out.println("쿠폰 생성 실패 : " + userId);
                }
            }
        }
    }

}
