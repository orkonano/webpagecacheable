package products.controller

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.annotation.MicronautTest
import io.micronaut.test.annotation.MockBean
import products.model.Product
import products.service.ProductService
import products.service.ProductServiceImpl
import reactor.core.publisher.Mono
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class ProductControllerSpec extends Specification {

    @Shared @AutoCleanup EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer)
    @Shared @AutoCleanup RxHttpClient client = embeddedServer.applicationContext.createBean(RxHttpClient, embeddedServer.getURL())

    @Inject
    ProductService productService

    void "test list all"() {
        when:
        HttpResponse response = client.toBlocking().exchange("/products")

        then:
        1 * productService.list() >> Mono.just([new Product(id: UUID.randomUUID(), name: "Test Product", price: 44.4)])
        response.status == HttpStatus.OK
        response.body().size() == 2
    }

    @MockBean(ProductServiceImpl)
    ProductService productService() {
        Mock(ProductService)
    }

}
