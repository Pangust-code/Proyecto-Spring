package ec.edu.ups.icc.fundamentos01.products.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductsEntity;

public class Product {

    private int id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Constructor básico para nuevos productos
     */
    public Product(int id, String name, BigDecimal price, Integer stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = null;  // Se asignará por PrePersist
        this.updatedAt = null;  // Se asignará por PreUpdate
    }

    /**
     * Constructor completo incluyendo fechas (para conversiones desde entidad)
     */
    public Product(int id, String name, BigDecimal price, Integer stock, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Crea un Product desde una entidad persistente
     * @param entity Entidad recuperada de la BD
     * @return instancia de Product para lógica de negocio
     */
    public static Product fromEntity(ProductsEntity entity) {
        return new Product(
            entity.getId().intValue(),
            entity.getName(),
            entity.getPrice(),
            entity.getStock(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    /**
     * Convierte este Product a una entidad persistente
     * @return ProductsEntity lista para guardar en BD
     */
    public ProductsEntity toEntity() {
        ProductsEntity entity = new ProductsEntity();
        if (this.id > 0) {
            entity.setId((long) this.id);
        }
        entity.setName(this.name);
        entity.setPrice(this.price);
        entity.setStock(this.stock);
        
        // Preservar fechas si existen (para actualizaciones)
        if (this.createdAt != null) {
            entity.setCreatedAt(this.createdAt);
        }
        if (this.updatedAt != null) {
            entity.setUpdatedAt(this.updatedAt);
        }
        
        return entity;
    }

    public ProductsResponseDto toResponseDto() {
        ProductsResponseDto dto = new ProductsResponseDto();
        dto.id = this.id;
        dto.name = this.name;
        dto.price = this.price;
        dto.stock = this.stock;
        dto.createdAt = this.createdAt;
        dto.updatedAt = this.updatedAt;
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

    // ==================== GETTERS Y SETTERS ====================

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
