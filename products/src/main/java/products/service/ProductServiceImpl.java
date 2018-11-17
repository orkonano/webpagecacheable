package products.service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import products.model.Product;
import products.redis.SerializedObjectCodec;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Inject private RedisClient redisClient;
    private volatile int productIdInsert = 1;

    @Override
    public Product create(Product product){
        StatefulRedisConnection<String, Object> connection = redisClient.connect(new SerializedObjectCodec());
        RedisCommands<String, Object> commands = connection.sync();
        product.setId(productIdInsert++);
        log.info("Creating the product with id {}", product.getId());
        String result = commands.set(product.getId().toString(), product);
        log.info("The product {} was created {}", product.getId(), result);
        return product;
    }

    @Override
    public Product update(Product product) {
        StatefulRedisConnection<String, Object> connection = redisClient.connect(new SerializedObjectCodec());
        RedisCommands<String, Object> commands = connection.sync();
        log.info("Updating the product with id {}", product.getId());
        String result = commands.set(product.getId().toString(), product);
        log.info("The product {} was updated {}", product.getId(), result);
        return product;
    }

    @Override
    public Optional<Product> getById(Integer id){
        StatefulRedisConnection<String, Object> connection = redisClient.connect(new SerializedObjectCodec());
        RedisCommands<String, Object> commands = connection.sync();
        return Optional.ofNullable((Product) commands.get(id.toString()));
    }

    @Override
    public Flowable<List<Product>> list() {
        StatefulRedisConnection<String, Object> connection = redisClient.connect(new SerializedObjectCodec());
        RedisCommands<String, Object> commands = connection.sync();
        List<String> ids = new ArrayList<>();
        for (int i = 1; i < productIdInsert; i++) {
            ids.add(String.valueOf(i));
        }
        log.info("Listing products ids {}", ids);
        return Flowable.just(ids.isEmpty() ? Collections.EMPTY_LIST : commands.mget(ids.toArray(new String[0])).stream().map(kv -> (Product) kv.getValue())
                .collect(Collectors.toList()));
    }
}