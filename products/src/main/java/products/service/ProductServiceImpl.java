package products.service;

import lombok.extern.slf4j.Slf4j;
import products.model.Product;
import products.repository.ProductRepository;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
@Slf4j
public class ProductServiceImpl implements ProductService {

    private volatile int productIdInsert = 1;

    @Inject private ProductRepository productRepository;

    @Override
    public Mono<Product> create(Product product){
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> update(Product product) {
        log.info("Updating the product with id {}", product.getId());
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> getById(String id){
        return productRepository.getById(id);
    }

    @Override
    public Mono<List<Product>> list() {
        return productRepository.all();
    }
}