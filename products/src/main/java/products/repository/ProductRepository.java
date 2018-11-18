package products.repository;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
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
        if (product.getId() != null){
            Product productMono = commands.get(product.getId()).block();
            if (productMono != null) {
                return commands.set(product.getId(), product)
                        .map(result -> product);
            }else {
                return Mono.empty();
            }
        }else {
            product.setId(UUID.randomUUID().toString());
            log.info("Creating the product with id {}", product.getId());
            Mono<Product> productMono = commands.set(product.getId(), product)
                    .map(success -> product);
            productMono.subscribe(result -> log.info("The product {} was created {}", product.getId(), result));
            return productMono;
        }
    }

    public Mono<Product> getById(String id){
        RedisReactiveCommands<String, Product> commands = redisConnection.reactive();
        return commands.get(id);
    }
}