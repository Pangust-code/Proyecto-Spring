package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateProductsDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    public String name;

    @NotBlank(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio debe ser un valor positivo")   
    public BigDecimal price;

    @NotBlank(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock debe ser un valor positivo")
    public Integer stock;

    @NotNull(message = "El ID del usuario es obligatorio")
    public Long userId;

    @NotNull(message = "El ID de la categoria es obligatorio")
    public Long categoriaId;

}
