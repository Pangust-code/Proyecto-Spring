package ec.edu.ups.icc.fundamentos01.categories.services;

import java.util.List;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.CreateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.UpdateCategoryDto;

public interface CategoryService {

    List<CategoriaResponseDto> findAll();

    CategoriaResponseDto findOne(Long id);

    CategoriaResponseDto create(CreateCategoryDto dto);

    CategoriaResponseDto update(Long id, UpdateCategoryDto dto);

    void delete(Long id);
    
} 