package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;


public interface ProductsService {
    
    List<ProductsResponseDto> findAll();

    Object findOne(int id);

    ProductsResponseDto create(ProductsResponseDto dto);

    Object update(int id, UpdateProductsDto dto);

    Object partialUpdate(int id, PartialUpdateProductsDto dto);
    Object delete(int id);
}
