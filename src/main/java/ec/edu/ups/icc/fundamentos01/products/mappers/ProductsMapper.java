package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductsEntity;
import ec.edu.ups.icc.fundamentos01.products.models.Product;

public class ProductsMapper {
    
    /**
     * Convierte un DTO de creación a un modelo Product
     */
    public static Product fromCreateDto(CreateProductsDto dto) {
        Product product = new Product(dto.name, dto.price, dto.stock);
        return product;
    }

    /**
     * Convierte un Product a un DTO de respuesta
     * @param product Modelo de dominio
     * @return DTO con los datos públicos del producto
     */
    public static ProductsResponseDto toResponse(Product product) {
        ProductsResponseDto dto = new ProductsResponseDto();
        dto.id = product.getId();
        dto.name = product.getName();
        dto.price = product.getPrice();
        dto.stock = product.getStock();
        dto.createdAt = product.getCreatedAt().toString();
        return dto;
    }

        /**
     * Convierte ProductEntity a ProductResponseDto (con relaciones completas)
     */
    public static ProductsResponseDto toResponse(ProductsEntity entity) {
        ProductsResponseDto dto = new ProductsResponseDto();

        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.price = entity.getPrice();
        dto.stock = entity.getStock();

        ProductsResponseDto.UserSummaryDto userDto = new ProductsResponseDto.UserSummaryDto();
        userDto.id = entity.getOwner().getId();
        userDto.name = entity.getOwner().getName();
        userDto.email = entity.getOwner().getEmail();
        dto.userId = userDto;

        dto.categories = entity.getCategories().stream()
                .map(ProductsMapper::toCategoryResponseDto)
                .sorted((left, right) -> left.name.compareToIgnoreCase(right.name))
                .toList();

        dto.createdAt = entity.getCreatedAt().toString();
        dto.updatedAt = entity.getUpdatedAt().toString();

        return dto;
    }

    private static CategoriaResponseDto toCategoryResponseDto(CategoryEntity category) {
        CategoriaResponseDto dto = new CategoriaResponseDto();
        dto.id = category.getId();
        dto.name = category.getName();
        dto.description = category.getDescription();
        return dto;
    }
}
