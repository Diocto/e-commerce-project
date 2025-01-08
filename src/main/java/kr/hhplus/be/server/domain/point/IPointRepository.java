package kr.hhplus.be.server.domain.point;

import java.util.Optional;

public interface IPointRepository {
    void save(Point point);
    Optional<Point> findById(Long id);
    void deleteById(Long id);
}
