package ec.edu.ups.icc.fundamentos01.categories.mappers;


import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.CreateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.models.Category;


public class CategoryMapper {

    /**
     * Convierte CreateCategoryDto a modelo de dominio
     */
    public static Category fromCreateDto(CreateCategoryDto dto) {
        return Category.fromDto(dto);
    }

    /**
     * Convierte modelo de dominio a CategoryResponseDto
     */
    public static CategoriaResponseDto toResponse(Category category) {
        CategoriaResponseDto dto = new CategoriaResponseDto();
        dto.id = category.getId();
        dto.name = category.getName();
        dto.description = category.getDescription();
        return dto;
    }    

}
