package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.List;

import ec.edu.ups.icc.fundamentos01.products.dtos.*;


public interface ProductsService {
    
    List<ProductsResponseDto> findAll();

    ProductsResponseDto findOne(Long id);

    ProductsResponseDto create(CreateProductsDto dto);

    ProductsResponseDto update(Long id, UpdateProductsDto dto);
 
    ProductsResponseDto partialUpdate(Long id, PartialUpdateProductsDto dto);

    void delete(Long id);

}
