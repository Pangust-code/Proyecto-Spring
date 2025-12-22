package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.entities.Products;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductsMapper;

@Service
public class ProductsServiceImpl implements ProductsService {

    private List<Products> products = new ArrayList<>();
    private int currentId = 1;

    @Override
    public List<ProductsResponseDto> findAll() {
        return products.stream()
                .map(ProductsMapper::toResponse)
                .toList();
    }

    @Override
    public Object findOne(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .map(ProductsMapper::toResponse)
                .orElseGet(() -> new ProductsResponseDto() { public String error = "Product not found"; });
    }

    @Override
    public ProductsResponseDto create(ProductsResponseDto dto) {
        Products product = ProductsMapper.toEntity(currentId++, dto.name, dto.price, currentId);
        products.add(product);
        return ProductsMapper.toResponse(product);
    }

    @Override
    public Object update(int id, UpdateProductsDto dto) {
        Products product = products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if (product == null) return new Object() { public String error = "Product not found"; };

        product.setName(dto.name);
        product.setPrice(dto.price);
        return ProductsMapper.toResponse(product);
    }

    @Override
    public Object partialUpdate(int id, PartialUpdateProductsDto dto) {
        Products product = products.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        if (product == null) return new Object() { public String error = "Product not found"; };

        if (dto.name != null) product.setName(dto.name);
        if (dto.price != null) product.setPrice(dto.price);

        return ProductsMapper.toResponse(product);
    }

    @Override
    public Object delete(int id) {
        boolean removed = products.removeIf(u -> u.getId() == id);
        if (!removed) return new Object() { public String error = "Product not found"; };

        return new Object() { public String message = "Deleted successfully"; };
    }

}
