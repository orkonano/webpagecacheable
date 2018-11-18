package products.service;

import products.model.Product;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductService {

    Mono<Product> create(Product product);

    Mono<Product> update(Product product);

    Mono<Product> getById(String id);

    Mono<List<Product>> list();

}
