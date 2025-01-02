package kr.hhplus.be.server.interfaces.api.point;

import kr.hhplus.be.server.interfaces.dto.point.PointChargeRequestBody;
import kr.hhplus.be.server.interfaces.dto.point.PointChargeResponseBody;
import kr.hhplus.be.server.interfaces.dto.point.PointViewResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/points")
public class PointController {
    @PostMapping()
    public ResponseEntity<PointChargeResponseBody> chargePoint(@RequestBody PointChargeRequestBody requestBody) {
        return ResponseEntity.ok(new PointChargeResponseBody(1000L));
    }

    @GetMapping()
    public ResponseEntity<PointViewResponseBody> getPoint(@RequestBody PointViewResponseBody requestBody) {
        return ResponseEntity.ok(new PointViewResponseBody(1000L));
    }

}
