package products.repository;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import products.model.Product;
import products.redis.SerializedObjectCodec;
import reactor.core.publisher.Mono;

import javax.inject.Singleton;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Singleton
public class ProductRepository {

    private final StatefulRedisConnection<String, Product> redisConnection;

    public ProductRepository(RedisClient redisClient) {
        this.redisConnection = redisClient.connect(new SerializedObjectCodec());;
    }

    public Mono<List<Product>> all(){
        RedisReactiveCommands<String, Product> commands = redisConnection.reactive();
        return commands.keys("*").buffer()
                .flatMap(s -> commands.mget(s.toArray(new String[0])))
                .filter(kv -> kv.hasValue())
                .map(kv -> kv.getValue())
                .collectList();
    }

    public Mono<Product> save(Product product){
        RedisReactiveCommands<String, Product> commands = redisConnection.reactive();
        Mono<String> saved = null;
        if (product.getId() != null){
            log.info("Updating the product with id {}", product.getId());
            saved = commands.get(product.getId())
                    .doOnNext(p -> log.info("Retrieved the product {}", p))
                    .flatMap(p -> commands.set(product.getId(), product));
        }else {
            product.setId(UUID.randomUUID().toString());
            log.info("Creating the product with id {}", product.getId());
            saved = commands.set(product.getId(), product);
        }
        return saved
                .doOnSuccess(p -> log.info("The product {} was saved successfully", p))
                .doOnError(t -> log.error("Error saving product {}", product, t))
                .map(result -> product);
    }

    public Mono<Product> getById(String id){
        RedisReactiveCommands<String, Product> commands = redisConnection.reactive();
        return commands.get(id);
    }
}