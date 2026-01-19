package ec.edu.ups.icc.fundamentos01.products.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.exceptions.domain.BadRequestException;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductsEntity;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;

public class Product {

    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private LocalDateTime createdAt;

    /**
     * Constructor básico para nuevos productos
     */
    public Product(Long id, String name, BigDecimal price, Integer stock) {

        if (name == null || name.isBlank())
            throw new BadRequestException("Nombre inválido");
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0)
            throw new BadRequestException("Precio inválido");
        if (stock == null || stock < 0)
            throw new BadRequestException("Stock inválido");
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = null;  // Se asignará por PrePersist
    }

    /**
     * Constructor completo incluyendo fechas (para conversiones desde entidad)
     */
    public Product(Long id, String name, BigDecimal price, Integer stock, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
    }

    public Product(String name, BigDecimal price, Integer stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Crea un Product desde una entidad persistente
     * @param entity Entidad recuperada de la BD
     * @return instancia de Product para lógica de negocio
     */
    public static Product fromEntity(ProductsEntity entity) {
        return new Product(
            entity.getId(),
            entity.getName(),
            entity.getPrice(),
            entity.getStock(),
            entity.getCreatedAt()
        );
    }

    /**
     * Convierte este Product a una entidad persistente
     * @return ProductsEntity lista para guardar en BD
     */
    public ProductsEntity toEntity() {
        ProductsEntity entity = new ProductsEntity();
        if (this.id != null && this.id > 0) {
            entity.setId(this.id);
        }
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setStock(this.stock);
        
        // Preservar fechas si existen (para actualizaciones)
        if (this.createdAt != null) {
            entity.setCreatedAt(this.createdAt);
        }
        
        return entity;
    }

    public ProductsResponseDto toResponseDto() {
        ProductsResponseDto dto = new ProductsResponseDto();
        dto.id = this.id;
        dto.name = this.name;
        dto.price = this.price;
        dto.stock = this.stock;
        dto.createdAt = this.createdAt == null ? null : this.createdAt.toString();
        return dto;
    }

    public Product update(UpdateProductsDto dto) {
        this.name = dto.name;
        this.price = dto.price;
        this.stock = dto.stock;
        return this;
    }

    public Product partialUpdate(PartialUpdateProductsDto dto) {
        if (dto.name != null) {
            this.name = dto.name;
        }
        
        if (dto.price != null) {
            this.price = dto.price;
        }
        
        
        if (dto.stock != null) {
            this.stock = dto.stock;
        }

        return this;
    }

    public static Product fromDto(CreateProductsDto dto){
        return new Product(dto.name, dto.price, dto.stock);
    }

    public ProductsEntity toEntity(UserEntity owner, CategoryEntity categoryEntity){
        ProductsEntity entity = new ProductsEntity();

        if (this.id != null && this.id > 0){
            entity.setId(this.id);
        }

        entity.setName(this.name);

        entity.setPrice(this.price);

        entity.setStock(this.stock);

        entity.setOwner(owner);

        entity.setCategories(Set.of(categoryEntity));

        return entity;
    }

    public ProductsEntity toEntity(UserEntity owner) {
        ProductsEntity entity = new ProductsEntity();
        if (this.id != null && this.id > 0) {
            entity.setId(this.id);
        }
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setStock(this.stock);
        entity.setOwner(owner);
        return entity;
    }
    // ==================== GETTERS Y SETTERS ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
