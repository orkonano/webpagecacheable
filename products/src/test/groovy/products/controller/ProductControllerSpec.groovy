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
        noExceptionThrown()
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
        noExceptionThrown()
        result
        result.id == idToretrieve.toString()
    }

    void "test create"() {
        given: "Define a new product without errors"
        def productWithoutError = new Product(name: "Test Product", price: 44.4)

        and: "Define a new product with errors"
        def productWithError = new Product(price: 44.4)

        when: "Create a product without errors"
        Product result = client.toBlocking().retrieve(HttpRequest.POST("/products", productWithoutError), Product)

        then:
        1 * productService.create(_) >>{ args ->
            Product product = args[0]
            product.setId(UUID.randomUUID().toString())
            return Mono.just(product)
        }
        noExceptionThrown()
        result
        result.id
        productWithoutError.name == result.name

        when: "Create a product with errors"
        client.toBlocking().exchange(HttpRequest.POST("/products", productWithError))

        then:
        def e = thrown(HttpClientResponseException)

        when:
        def response = e.response

        then:
        response.status == HttpStatus.BAD_REQUEST
    }

    void "test update Product"() {
        given: "Select an id product to update"
        def idToUpdate = UUID.randomUUID().toString()

        and: "define a producto without errors"
        def productoWithoutError = new Product(name: "Test Product Updated", price: 50.4)

        and: "define a product with errors"
        def productWithError = new Product(price: 50.4)

        when: "Update a product without error"
        Product result = client.toBlocking().retrieve(HttpRequest.PUT("/products/${idToUpdate}",
                productoWithoutError), Product)

        then:
        1 * productService.update(_) >>{ args ->
            return Mono.just(args[0])
        }
        noExceptionThrown()
        result
        result.id
        productoWithoutError.name == result.name

        when: "Update a producto with error"
        client.toBlocking().retrieve(HttpRequest.PUT("/products/${idToUpdate}",
                productWithError), Product)

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
