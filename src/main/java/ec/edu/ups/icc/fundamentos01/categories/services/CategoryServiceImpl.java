package ec.edu.ups.icc.fundamentos01.categories.services;

import java.util.List;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.CreateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.dtos.UpdateCategoryDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.mappers.CategoryMapper;
import ec.edu.ups.icc.fundamentos01.categories.models.Category;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exceptions.domain.NotFoundException;

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryServiceImpl(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<CategoriaResponseDto> findAll() {
        return categoryRepo.findAll()
                .stream()
                .map(Category::fromEntity)
                .map(CategoryMapper::toResponse)
                .toList();
    }

    @Override
    public CategoriaResponseDto findOne(Long id) {
        return categoryRepo.findById(id)
                .map(Category::fromEntity)
                .map(CategoryMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));
    }

    @Override
    public CategoriaResponseDto create(CreateCategoryDto dto) {

        // Validar que el nombre sea único
        if (categoryRepo.existsByName(dto.name)) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + dto.name);
        }

        // Crear modelo de dominio
        Category category = Category.fromDto(dto);

        // Convertir a entidad y persistir
        CategoryEntity entity = category.toEntity();
        CategoryEntity saved = categoryRepo.save(entity);

        // Retornar DTO de respuesta
        return CategoryMapper.toResponse(Category.fromEntity(saved));
    }

    @Override
    public CategoriaResponseDto update(Long id, UpdateCategoryDto dto) {

        // Buscar categoría existente
        CategoryEntity existing = categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));

        // Validar que el nuevo nombre sea único (si cambió)
        if (!existing.getName().equals(dto.name) && categoryRepo.existsByName(dto.name)) {
            throw new ConflictException("Ya existe otra categoría con el nombre: " + dto.name);
        }

        // Actualizar usando dominio
        Category category = Category.fromEntity(existing);
        category.update(dto);

        // Persistir cambios
        CategoryEntity updated = category.toEntity();
        updated.setId(id); // Mantener el ID
        CategoryEntity saved = categoryRepo.save(updated);

        return CategoryMapper.toResponse(Category.fromEntity(saved));
    }

    @Override
    public void delete(Long id) {

        CategoryEntity category = categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + id));

        // Eliminación física
        categoryRepo.delete(category);
    }

}
