package kr.hhplus.be.server.domain.point;

import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final IPointRepository pointRepository;

    PointService(IPointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public Point charge(Long userId, Long point) {
        Point pointEntity = pointRepository.findById(userId).orElse(Point.builder().id(userId).point(0L).userId(userId).build());
        pointEntity.charge(point);
        pointRepository.save(pointEntity);
        return pointEntity;
    }

    /// 주문서 결제 처리시 use 가 존재하기 때문에 service 에서는 use 함수가 존재한다.
    public Point use(Long userId, Long point) {
        Point pointEntity = pointRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("포인트가 없습니다"));
        pointEntity.use(point);
        pointRepository.save(pointEntity);
        return pointEntity;
    }

    public Point get(Long userId) {
        return pointRepository.findById(userId).orElse(Point.builder().id(userId).point(0L).userId(userId).build());
    }
}
