package ec.edu.ups.icc.fundamentos01.products.mappers;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
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
}
