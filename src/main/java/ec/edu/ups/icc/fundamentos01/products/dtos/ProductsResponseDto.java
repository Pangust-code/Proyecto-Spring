package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.math.BigDecimal;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoriaResponseDto;

public class ProductsResponseDto {
    public Long id;
    public String name;
    public BigDecimal price;
    public Integer stock;
    public String createdAt;
    public String updatedAt;

    // aparesca sus categorias y su owner

    public UserSummaryDto userId;

    public CategoriaResponseDto categoriaId;

    public static class UserSummaryDto {

        public int id;
        public String name;
        public String email;

    }

    public UserSummaryDto getUserId() {
        return userId;
    }

    public CategoriaResponseDto getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(CategoriaResponseDto categoriaId) {
        this.categoriaId = categoriaId;
    }

    

    
}
