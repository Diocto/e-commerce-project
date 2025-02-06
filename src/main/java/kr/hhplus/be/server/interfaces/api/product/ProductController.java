package kr.hhplus.be.server.interfaces.api.product;

import kr.hhplus.be.server.domain.product.PopularProducts;
import kr.hhplus.be.server.domain.product.ProductQuantityDto;
import kr.hhplus.be.server.domain.product.ProductUseCase;
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
        public record ProductViewList(
                List<ProductView> productViewList
        ) {}
        public record ProductView(
                Long id,
                String productName,
                Long price,
                String description,
                Long stockQuantity
        ){
        }
    }
    @GetMapping("/popular")
    public ResponseEntity<Response.ProductViewList> getProducts(
            @RequestParam Integer page,
            @RequestParam Integer size) {
        PopularProducts products = productUseCase.getPopularProducts(page, size);

        return ResponseEntity.ok(new Response.ProductViewList(
                products.popularProducts().stream()
                .map(productQuantityDto -> new Response.ProductView(
                        productQuantityDto.product().getId(),
                        productQuantityDto.product().getName(),
                        productQuantityDto.product().getPrice(),
                        productQuantityDto.product().getDescription(),
                        productQuantityDto.product().getStock()
                ))
                .toList()));
    }
}
