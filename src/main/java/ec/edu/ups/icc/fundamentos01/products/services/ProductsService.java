package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;


public interface ProductsService {
    
    List<ProductsResponseDto> findAll();

    ProductsResponseDto findOne(int id);

    ProductsResponseDto create(CreateProductsDto dto);

    ProductsResponseDto update(int id, UpdateProductsDto dto);
 
    ProductsResponseDto partialUpdate(int id, PartialUpdateProductsDto dto);

    void delete(int id);
}
