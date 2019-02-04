package api.gateway.fallback;

import api.gateway.client.ProductClient;
import api.gateway.models.product.Product;
import io.micronaut.retry.annotation.Fallback;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Fallback
@Slf4j
public class ProductClientFallback implements ProductClient {

    private static final Product defaultProduct = new Product();

    static{
        defaultProduct.setId("default");
        defaultProduct.setName("Default");
        defaultProduct.setPrice(0d);
    }

    @Override
    public Single<List<Product>> listAll() {
        log.info("Default product list");
        return Single.just(Arrays.asList(defaultProduct));
    }

    @Override
    public Single<Product> getById(String id) {
        return null;
    }

    @Override
    public Single<Product> create(Product product) {
        return null;
    }

    @Override
    public Single<Product> update(String id, Product product) {
        return null;
    }
}
