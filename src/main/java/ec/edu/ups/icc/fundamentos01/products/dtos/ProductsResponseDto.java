package ec.edu.ups.icc.fundamentos01.products.dtos;

import java.math.BigDecimal;
import java.util.List;

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

    public List<CategoriaResponseDto> categories;

    public static class UserSummaryDto {

        public Long id;
        public String name;
        public String email;

    }

    public UserSummaryDto getUserId() {
        return userId;
    }

    public List<CategoriaResponseDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoriaResponseDto> categories) {
        this.categories = categories;
    }

    

    
}
