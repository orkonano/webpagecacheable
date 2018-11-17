package products.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;
import io.reactivex.Flowable;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import products.model.Product;
import products.service.ProductService;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Validated
@Controller("/products")
public class ProductController {

    @Inject private ProductService productService;


    @Get("/")
    public Flowable<List<Product>> list() {
        return productService.list();
    }

    @Get("/{id}")
    public Single<Product> getById(@NotNull Integer id) {
        log.info("Request for product id: {}", id);
        return Single.just(productService.getById(id).orElse(new Product()));
    }

    @Post("/")
    public Single<Product> create(Product product){
        log.info("Creating product {}", product);
        return Single.just(productService.create(product));
    }

    @Put("/{id}")
    public Single<Product> update(Integer id, Product product){
        log.info("Updating product {} with values {}", id, product);
        product.setId(id);
        return Single.just(productService.update(product));
    }
}