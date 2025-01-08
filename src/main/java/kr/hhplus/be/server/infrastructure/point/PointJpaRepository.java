package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.IPointRepository;
import kr.hhplus.be.server.domain.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointJpaRepository extends JpaRepository<Point, Long>, IPointRepository {
}
