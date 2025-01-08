package kr.hhplus.be.server.interfaces.api.point;

import jakarta.websocket.server.PathParam;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.point.PointUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/points")
public class PointController {
    private PointUseCase pointUseCase;

    public PointController(PointUseCase pointUseCase) {
        this.pointUseCase = pointUseCase;
    }

    public static class Request {
        public record PointCharge(
                Long point
        ) {}
    }

    public static class Response {
        public record PointCharge(
                Long totalPoint
        ) {
        }

        public record PointView(
                Long point
        ) {}
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Response.PointCharge> chargePoint(@PathVariable Long userId, @RequestBody Request.PointCharge requestBody) {
        Point userPoint = pointUseCase.charge(userId, requestBody.point());
        return ResponseEntity.ok(new Response.PointCharge(userPoint.getPoint()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response.PointView> getPoint(@PathVariable Long userId) {
        Point userPoint = pointUseCase.get(userId);
        return ResponseEntity.ok(new Response.PointView(userPoint.getPoint()));
    }
}
