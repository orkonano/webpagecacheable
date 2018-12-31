package api.gateway.controller;

import api.gateway.client.ProductClient;
import api.gateway.models.product.Product;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller("/products")
@Slf4j
public class ProductController {

    @Inject
    ProductClient productClient;

    @Get()
    public Single<List<Product>> listAll() {
        log.info("Listing all products from products api");
        return productClient.listAll();
    }

    @Get("/{id}")
    public Single<Product> getById(@NotNull @NotEmpty String id) {
        log.info("Request for product id: {} from products api", id);
        return productClient.getById(id);
    }

    @Post()
    public Single<Product> create(Product product){
        log.info("Creating product {} to product api", product);
        return productClient.create(product);
    }

    @Put("/{id}")
    public Single<Product> update(String id, Product product){
        log.info("Updating product {} with values {} to product api", id, product);
        product.setId(id);
        return productClient.update(id, product);
    }
}