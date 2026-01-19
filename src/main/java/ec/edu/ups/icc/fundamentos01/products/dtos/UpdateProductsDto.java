package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateProductsDto {

    @NotBlank
    @Size(min = 3, max = 150)
    public String name;

    @NotBlank
    @Min(value = 0)
    public BigDecimal price;

    @NotBlank
    @Min(value = 0)
    public Integer stock;

    @NotBlank
    @Size(min = 1)
    public Set<Long> categoryId;
}
