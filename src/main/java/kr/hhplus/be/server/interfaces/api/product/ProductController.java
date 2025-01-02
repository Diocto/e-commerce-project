package kr.hhplus.be.server.interfaces.api.product;

import jakarta.websocket.server.PathParam;
import kr.hhplus.be.server.interfaces.dto.product.ProductViewResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @GetMapping("/{product_id}")
    public ResponseEntity<ProductViewResponseBody> getProduct(@PathParam("product_id") Long productId) {
        return ResponseEntity.ok(new ProductViewResponseBody(1L, "productName", 1000L, "description", 10L));
    }
}
