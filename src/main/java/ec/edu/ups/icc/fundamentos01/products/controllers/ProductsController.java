package ec.edu.ups.icc.fundamentos01.products.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.services.ProductsService;

import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductsService productService;

    public ProductsController(ProductsService productService) {
        this.productService = productService;
    }

        @GetMapping
    public List<ProductsResponseDto> findAll() {
        return productService.findAll();
    }

        @GetMapping("/{id}")
    public ProductsResponseDto findOne(@PathVariable("id") int id) {
        return productService.findOne(id);
    }

        @PostMapping
    public ProductsResponseDto create(@RequestBody CreateProductsDto dto) {
        return productService.create(dto);
    }

        @PutMapping("/{id}")
    public ProductsResponseDto update(@PathVariable("id") int id, @RequestBody UpdateProductsDto dto) {
        return productService.update(id, dto);
    }

        @PatchMapping("/{id}")
    public ProductsResponseDto partialUpdate(@PathVariable("id") int id, @RequestBody PartialUpdateProductsDto dto) {
        return productService.partialUpdate(id, dto);
    }

        @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        productService.delete(id);
    }
    
}