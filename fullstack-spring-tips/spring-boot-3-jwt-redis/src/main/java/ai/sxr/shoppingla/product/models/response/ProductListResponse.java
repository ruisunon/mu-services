package ai.sxr.shoppingla.product.models.response;


import ai.sxr.shoppingla.product.models.Product;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class ProductListResponse {
    private List<Product> data;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ProductListResponse(@JsonProperty("data") List<Product> data) {
        this.data = data;
    }
}