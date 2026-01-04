package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class PartialUpdateProductsDto {

    @Size(min = 3, max = 150)
    public String name;

    @Min(value = 0)
    public BigDecimal price;

    @Min(value = 0)
    public Integer stock;
}
