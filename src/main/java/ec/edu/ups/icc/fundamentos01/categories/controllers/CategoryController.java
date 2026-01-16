package ec.edu.ups.icc.fundamentos01.categories.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.CreateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.UpdateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.services.CategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
        private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDto>> findAll() {
        List<CategoriaResponseDto> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> findById(@PathVariable Long id) {
        CategoriaResponseDto category = categoryService.findOne(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDto> create(@Valid @RequestBody CreateCategoryDto dto) {
        CategoriaResponseDto created = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryDto dto) {
        CategoriaResponseDto updated = categoryService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
