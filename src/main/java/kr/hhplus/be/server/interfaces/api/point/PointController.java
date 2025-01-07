package kr.hhplus.be.server.interfaces.api.point;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/points")
public class PointController {
    public static class Request {
        public record PointCharge(
                Long userId,
                Long point
        ) {}

        public record PointView(
                Long userId
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

    @PostMapping()
    public ResponseEntity<Response.PointCharge> chargePoint(@RequestBody Request.PointCharge requestBody) {
        return ResponseEntity.ok(new Response.PointCharge(1000L));
    }

    @GetMapping()
    public ResponseEntity<Response.PointView> getPoint(@RequestBody Request.PointView requestBody) {
        return ResponseEntity.ok(new Response.PointView(1000L));
    }

}
