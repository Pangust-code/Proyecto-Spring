package ec.edu.ups.icc.fundamentos01.categories.dtos;

import jakarta.validation.constraints.Size;

public class PartialUpdateCategoryDto {
    
    @Size(min = 3, max = 150)
    public String name;

    @Size(min = 3, max = 150)
    public String description;

}
