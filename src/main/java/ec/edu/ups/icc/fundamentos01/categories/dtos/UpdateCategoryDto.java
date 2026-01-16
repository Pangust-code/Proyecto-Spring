package ec.edu.ups.icc.fundamentos01.categories.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateCategoryDto {

    @NotBlank
    @Size(min = 3, max = 150)
    public String name;

    @NotBlank
    @Size(min = 3, max = 150)
    public String description;

}
