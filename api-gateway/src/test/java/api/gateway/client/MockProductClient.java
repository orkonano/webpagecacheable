package api.gateway.client;

import api.gateway.models.product.Product;
import io.micronaut.context.annotation.Primary;
import io.reactivex.Single;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Singleton
@Primary
public class MockProductClient implements ProductClient{
    private static final Product defaultProduct = new Product();

    static{
        defaultProduct.setId("default");
        defaultProduct.setName("Default");
        defaultProduct.setPrice(0d);
    }

    @Override
    public Single<List<Product>> listAll() {
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
