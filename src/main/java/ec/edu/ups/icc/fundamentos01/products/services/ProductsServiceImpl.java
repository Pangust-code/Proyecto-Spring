package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
import ec.edu.ups.icc.fundamentos01.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductsDto;
import ec.edu.ups.icc.fundamentos01.products.entities.ProductsEntity;
import ec.edu.ups.icc.fundamentos01.products.models.Product;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductsRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;

@Service
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepo;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;


    public ProductsServiceImpl(ProductsRepository productsRepo, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.productsRepo = productsRepo;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }


    private Set<CategoryEntity> validateAndGetCategories(Set<Long> categoryIds) {
        Set<CategoryEntity> categories = new HashSet<>();
        for (Long categoryId : categoryIds) {
            CategoryEntity category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + categoryId));
            categories.add(category);
        }
        return categories;
    }

    private CategoryEntity validateCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada con ID: " + categoryId));
    }



    // Actualizar código de los métodos para usar productsRepo
    //
    // @Override
    // public List<UserResponseDto> findAll() {

    // // 1. El repositorio devuelve entidades JPA (UserEntity)
    // return userRepo.findAll()
    // .stream()

    // // 2. Cada UserEntity se transforma en un modelo de dominio User
    // // Aquí se desacopla la capa de persistencia de la lógica de negocio
    // .map(User::fromEntity)

    // // 3. El modelo de dominio se convierte en DTO de respuesta
    // // Solo se exponen los campos permitidos por la API
    // .map(UserMapper::toResponse)

    // // 4. Se recopila el resultado final como una lista de DTOs
    // .toList();
    // }

    // Forma iterativa tradicional
    @Override
    public List<ProductsResponseDto> findAll() {

        // Lista final que se devolverá al controlador
        List<ProductsResponseDto> response = new ArrayList<>();

        // 1. Obtener todas las entidades desde la base de datos
        List<ProductsEntity> entities = productsRepo.findAll();

        // 2. Iterar sobre cada entidad
        for (ProductsEntity entity : entities) {

            // 3. Convertir la entidad en modelo de dominio
            Product product = Product.fromEntity(entity);

            // 4. Convertir el modelo de dominio en DTO de respuesta
            ProductsResponseDto dto = product.toResponseDto();

            // 5. Agregar el DTO a la lista de resultados
            response.add(dto);
        }

        // 6. Retornar la lista final de DTOs
        return response;
    }

    @Override
    public ProductsResponseDto findOne(Long id) {
        return productsRepo.findById((long) id)
                .map(Product::fromEntity)
                .map(Product::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Producto con id: " + id + " no encontrado"));
    }

    @Override
    public ProductsResponseDto create(CreateProductsDto dto) {
        if (productsRepo.findByName(dto.name).isPresent()) {
            throw new ConflictException("Ya existe un producto con el nombre: " + dto.name);
        }

        UserEntity owner = userRepository.findById(dto.userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + dto.userId));

        Set<CategoryEntity> categories = validateAndGetCategories(dto.categoriaId);

        Product newProduct = Product.fromDto(dto);
        ProductsEntity entity = newProduct.toEntity(owner);
        entity.setCategories(categories);

        ProductsEntity saved = productsRepo.save(entity);

        return toResponseDto(saved);
    }
    

    private ProductsResponseDto toResponseDto(ProductsEntity entity){

        ProductsResponseDto dto = new ProductsResponseDto();

        // Campos básicos
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.price = entity.getPrice();
        dto.stock = entity.getStock();

        ProductsResponseDto.UserSummaryDto userDto = new ProductsResponseDto.UserSummaryDto();
        userDto.id = entity.getOwner().getId();
        userDto.name = entity.getOwner().getName();
        userDto.email = entity.getOwner().getEmail();
        dto.userId = userDto;

        dto.categories = entity.getCategories().stream()
                .map(this::toCategoryResponseDto)
                .sorted((left, right) -> left.name.compareToIgnoreCase(right.name))
                .toList();

        dto.createdAt = entity.getCreatedAt().toString();
        dto.updatedAt = entity.getUpdatedAt().toString();

        return dto;
    }

    private CategoriaResponseDto toCategoryResponseDto(CategoryEntity category) {
        CategoriaResponseDto dto = new CategoriaResponseDto();
        dto.id = category.getId();
        dto.name = category.getName();
        dto.description = category.getDescription();
        return dto;
    }

    @Override
    public ProductsResponseDto update(Long id, UpdateProductsDto dto) {

        productsRepo.findByName(dto.name).ifPresent(existing -> {
            if (existing.getId() != id) {
                throw new ConflictException("Ya existe otro producto con el nombre: " + dto.name);
            }
        });

        ProductsEntity existingEntity = productsRepo.findById((long) id)
                .orElseThrow(() -> new NotFoundException("Producto con id: " + id + " no encontrado"));

        existingEntity.setName(dto.name);
        existingEntity.setPrice(dto.price);
        existingEntity.setStock(dto.stock);

        Set<CategoryEntity> categories = validateAndGetCategories(dto.categoryId);
        existingEntity.clearCategories();
        existingEntity.setCategories(categories);

        ProductsEntity saved = productsRepo.save(existingEntity);
        return toResponseDto(saved);
    }

    @Override
    public ProductsResponseDto partialUpdate(Long id, PartialUpdateProductsDto dto) {

        ProductsEntity existingEntity = productsRepo.findById((long) id)
                .orElseThrow(() -> new NotFoundException("Producto con id: " + id + " no encontrado"));

        if (dto.name != null) {
            existingEntity.setName(dto.name);
        }
        
        if (dto.price != null) {
            existingEntity.setPrice(dto.price);
        }
        if (dto.stock != null) {
            existingEntity.setStock(dto.stock);
        }

        ProductsEntity saved = productsRepo.save(existingEntity);
        return toResponseDto(saved);
    }

   @Override
    public void delete(Long id) {
          // Verifica existencia y elimina
        productsRepo.findById((long) id)
        .ifPresentOrElse(
            productsRepo::delete,
            () -> {
                throw new NotFoundException("Producto con id: " + id + " no encontrado");
            }
        );
    }

    @Override
    public boolean validateProductName(String name, int id) {
        productsRepo.findByName(name).ifPresent(existing -> {
            if (existing.getId() != id) {
                throw new ConflictException("Ya existe otro producto con el nombre: " + name);
            }
        });
        return true;
    }

    @Override
    public List<ProductsResponseDto> findByUserId(Long userId) {
        // Verificar que el usuario existe
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));

        return productsRepo.findByOwnerId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<ProductsResponseDto> findByCategoryId(Long categoryId) {
        validateCategory(categoryId);

        return productsRepo.findByCategories_Id(categoryId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }
}
