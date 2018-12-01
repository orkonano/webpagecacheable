package api.gateway.client;

import api.gateway.models.product.Product;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.client.annotation.Client;
import io.reactivex.Single;

import java.util.List;

@Client("http://localhost:8081/products")
public interface ProductClient {

    @Get()
    Single<List<Product>> listAll();

    @Get("/{id}")
    Single<Product> getById(String id);

    @Post
    Single<Product> create(Product product);

    @Put("/{id}")
    Single<Product> update(String id, Product product);
}
