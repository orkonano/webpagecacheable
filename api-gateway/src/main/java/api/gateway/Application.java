package api.gateway;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Api Gateway Orko",
                version = "0.0",
                description = "Api gateway of the orko company",
                license = @License(name = "Apache 2.0", url = "http://foo.bar"),
                contact = @Contact(name = "Orko", email = "orquito@gmail.com")
        )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}