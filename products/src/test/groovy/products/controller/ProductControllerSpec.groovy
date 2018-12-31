package products.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import products.model.Product
import products.service.ProductService
import products.service.ProductServiceImpl
import reactor.core.publisher.Mono
import spock.lang.Ignore
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class ProductControllerSpec extends Specification {

    @Inject
    @Client('/')
    RxHttpClient client

    @Inject
    ProductService productService

    void "test list all HttpResponse"() {
        when:
        HttpResponse response = client.toBlocking().exchange("/products")

        then:
        1 * productService.list() >>{
            return Mono.just([new Product(id: UUID.randomUUID(), name: "Test Product", price: 44.4)])
        }
        response.status == HttpStatus.OK
    }

    @Ignore
    void "test list all List of Product"() {
        when:
        List<Product> result = client.toBlocking().retrieve("/products", Product)

        then:
        1 * productService.list() >>{
            return Mono.just([new Product(id: UUID.randomUUID(), name: "Test Product", price: 44.4),
                       new Product(id: UUID.randomUUID(), name: "Test Product 2", price: 50)])
        }
        result.size() == 1
    }

    void "test get by id"() {
        given:
        def idToretrieve = UUID.randomUUID()

        when:
        Product result = client.toBlocking().retrieve("/products/${idToretrieve}", Product)

        then:
        1 * productService.getById(_) >>{ args ->
            return Mono.just(new Product(id: args[0], name: "Test Product", price: 44.4))
        }
        result
        result.id == idToretrieve.toString()
    }

    @Ignore
    void "test get by id with empty id"() {
        given:
        def idToretrieve = ""

        when:
        HttpResponse response = client.toBlocking().exchange("/products/${idToretrieve}")

        then:
//        0 * productService.getById(_) >>{ args ->
//            return Mono.just(new Product(id: args[0], name: "Test Product", price: 44.4))
//        }
        response.status == HttpStatus.BAD_REQUEST
    }

    void "test create"() {
        given:
        def newProduct = new Product(name: "Test Product", price: 44.4)

        when:
        Product result = client.toBlocking().retrieve(HttpRequest.POST("/products", newProduct), Product)

        then:
        1 * productService.create(_) >>{ args ->
            Product product = args[0]
            product.setId(UUID.randomUUID().toString())
            return Mono.just(product)
        }
        noExceptionThrown()
        result
        result.id
        newProduct.name == result.name
    }

    void "test create with errors"() {
        given:
        def newProduct = new Product(price: 44.4)

        when:
        client.toBlocking().exchange(HttpRequest.POST("/products", newProduct))

        then:
        def e = thrown(HttpClientResponseException)

        when:
        def response = e.response

        then:
        response.status == HttpStatus.BAD_REQUEST

    }

    @MockBean(ProductServiceImpl)
    ProductService productService() {
        Mock(ProductService)
    }

}
