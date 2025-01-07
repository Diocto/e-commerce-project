package kr.hhplus.be.server.interfaces.api.product;

import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    public static class Request {

    }

    public static class Response {
        public record ProductView(
                Long productId,
                String productName,
                Long price,
                String description,
                Long stockQuantity
        ){
        }
    }
    @GetMapping("/{product_id}")
    public ResponseEntity<Response.ProductView> getProduct(@PathParam("product_id") Long productId) {
        return ResponseEntity.ok(new Response.ProductView(1L, "productName", 1000L, "description", 10L));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Response.ProductView>> getPopularProduct() {
        List<Response.ProductView> productViewResponseBodies = List.of(
                new Response.ProductView(1L, "productName", 1000L, "description", 10L),
                new Response.ProductView(2L, "productName2", 2000L, "description2", 20L)
        );
        return ResponseEntity.ok(productViewResponseBodies);
    }

}
