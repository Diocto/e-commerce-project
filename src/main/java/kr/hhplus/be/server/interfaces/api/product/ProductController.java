package kr.hhplus.be.server.interfaces.api.product;

import jakarta.websocket.server.PathParam;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

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
    @GetMapping()
    public ResponseEntity<List<Response.ProductView>> getProducts(
            @RequestParam Integer page,
            @RequestParam Integer size) {
        List<Product> products = productUseCase.getProducts(page, size);
        return ResponseEntity.ok(products.stream()
                .map(product -> new Response.ProductView(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getDescription(),
                        product.getStock()
                ))
                .toList());
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
