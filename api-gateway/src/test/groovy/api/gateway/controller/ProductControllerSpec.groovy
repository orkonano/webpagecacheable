package api.gateway.controller

import api.gateway.client.ProductClient
import api.gateway.models.product.Product
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import io.reactivex.Single
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class ProductControllerSpec extends Specification {

    @Inject
    @Client('/')
    RxHttpClient client

    @Inject
    ProductClient productClient

    void "test list all HttpResponse"() {
        when:
        HttpResponse response = client.toBlocking().exchange("/products")

        then:
       // 1 * productClient.listAll()
        noExceptionThrown()
        response.status == HttpStatus.OK
    }
//    void "test get by id"() {
//        given:
//        def idToretrieve = UUID.randomUUID()
//
//        when:
//        Product result = client.toBlocking().retrieve("/products/${idToretrieve}", Product)
//
//        then:
//        1 * productClient.getById(_) >>{ args ->
//            return Mono.just(new Product(id: args[0], name: "Test Product", price: 44.4))
//        }
//        noExceptionThrown()
//        result
//        result.id == idToretrieve.toString()
//    }
//
//    void "test create"() {
//        given: "Define a new product without errors"
//        def productWithoutError = new Product(name: "Test Product", price: 44.4)
//
//        and: "Define a new product with errors"
//        def productWithError = new Product(price: 44.4)
//
//        when: "Create a product without errors"
//        Product result = client.toBlocking().retrieve(HttpRequest.POST("/products", productWithoutError), Product)
//
//        then:
//        1 * productClient.create(_) >>{ args ->
//            Product product = args[0]
//            product.setId(UUID.randomUUID().toString())
//            return Mono.just(product)
//        }
//        noExceptionThrown()
//        result
//        result.id
//        productWithoutError.name == result.name
//
//        when: "Create a product with errors"
//        client.toBlocking().exchange(HttpRequest.POST("/products", productWithError))
//
//        then:
//        def e = thrown(HttpClientResponseException)
//
//        when:
//        def response = e.response
//
//        then:
//        response.status == HttpStatus.BAD_REQUEST
//    }
//
//    void "test update Product"() {
//        given: "Select an id product to update"
//        def idToUpdate = UUID.randomUUID().toString()
//
//        and: "define a producto without errors"
//        def productoWithoutError = new Product(name: "Test Product Updated", price: 50.4)
//
//        and: "define a product with errors"
//        def productWithError = new Product(price: 50.4)
//
//        when: "Update a product without error"
//        Product result = client.toBlocking().retrieve(HttpRequest.PUT("/products/${idToUpdate}",
//                productoWithoutError), Product)
//
//        then:
//        1 * productClient.update(_) >>{ args ->
//            return Mono.just(args[0])
//        }
//        noExceptionThrown()
//        result
//        result.id
//        productoWithoutError.name == result.name
//
//        when: "Update a producto with error"
//        client.toBlocking().retrieve(HttpRequest.PUT("/products/${idToUpdate}",
//                productWithError), Product)
//
//        then:
//        def e = thrown(HttpClientResponseException)
//
//        when:
//        def response = e.response
//
//        then:
//        response.status == HttpStatus.BAD_REQUEST
//    }
}
