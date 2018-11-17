package products.service;

import io.reactivex.Flowable;
import products.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product create(Product product);

    Product update(Product product);

    Optional<Product> getById(Integer id);

    Flowable<List<Product>> list();

}
