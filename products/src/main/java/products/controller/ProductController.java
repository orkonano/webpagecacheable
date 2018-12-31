package products.controller;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import lombok.extern.slf4j.Slf4j;
import products.model.Product;
import products.service.ProductService;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@Controller("/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }


    @Get()
    public Mono<List<Product>> list() {
        log.info("Listing all products");
        return productService.list();
    }

    @Get("/{id}")
    public Mono<Product> getById(@NotNull @NotEmpty String id) {
        log.info("Request for product id: {}", id);
        return productService.getById(id);
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Mono<Product> create(@Body @Valid Product product){
        log.info("Creating product {}", product);
        return productService.create(product);
    }


    @Put("/{id}")
    public Mono<Product> update(String id, Product product){
        log.info("Updating product {} with values {}", id, product);
        product.setId(id);
        return productService.update(product);
    }
}