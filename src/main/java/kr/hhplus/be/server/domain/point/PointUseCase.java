package kr.hhplus.be.server.domain.point;

import org.springframework.stereotype.Service;

@Service
public class PointUseCase {
    private final PointService pointService;

    public PointUseCase(PointService pointService) {
        this.pointService = pointService;
    }

    public Point charge(Long userId, Long point) {
        return pointService.charge(userId, point);
    }

    public Point get(Long userId) {
        return pointService.get(userId);
    }

    /// 이용자 유스케이스 에서는 '포인트만' 사용하는 유스케이스는 존재하지 않기에, use 함수가 존재하지 않는다.
}
