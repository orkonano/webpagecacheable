package products.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class Product implements Serializable {

    private String id;

    @NotBlank
    private String name;

    private Double price;
}
