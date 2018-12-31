package products.service;

import products.model.Product;
import reactor.core.publisher.Mono;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public interface ProductService {

    Mono<Product> create(Product product);

    Mono<Product> update(Product product);

    Mono<Product> getById(String id);

    Mono<List<Product>> list();

}
