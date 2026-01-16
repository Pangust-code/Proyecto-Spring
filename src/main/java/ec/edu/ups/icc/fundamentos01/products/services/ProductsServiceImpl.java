package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;
import ec.edu.ups.icc.fundamentos01.categories.repositories.CategoryRepository;
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
        UserEntity owner = userRepository.findById(dto.userId)
                .orElseThrow(() -> new NotFoundException("usuario no existe"));

        CategoryEntity categoria = categoryRepository.findById(dto.categoriaId)
                .orElseThrow(() -> new NotFoundException("Categoria no existe"));

        Product newProduct = Product.fromDto(dto);
        
        ProductsEntity entity = newProduct.toEntity(owner, categoria);

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
        
        // Crear objeto User anidado
        ProductsResponseDto.UserSummaryDto userDto = new ProductsResponseDto.UserSummaryDto();
        userDto.id = entity.getOwner().getId().intValue();
        userDto.name = entity.getOwner().getName();
        userDto.email = entity.getOwner().getEmail();
        dto.userId = userDto;

        CategoriaResponseDto categoriaResponseDto = new CategoriaResponseDto();
        categoriaResponseDto.id = entity.getCategory().getId();
        categoriaResponseDto.name = entity.getCategory().getName();
        categoriaResponseDto.description = entity.getCategory().getDescription();
        dto.categoriaId = categoriaResponseDto;
        
        dto.createdAt = entity.getCreatedAt().toString();
        dto.updatedAt = entity.getUpdatedAt().toString();
        
        return dto;
    }

    @Override
    public ProductsResponseDto update(Long id, UpdateProductsDto dto) {

        return productsRepo.findById((Long) id)
                // Entity → Domain
                .map(Product::fromEntity)
                // Aplicar cambios permitidos en el dominio
                .map(product -> product.update(dto))

                // Domain → Entity
                .map(Product::toEntity)

                // Persistencia
                .map(productsRepo::save)

                // Entity → Domain
                .map(Product::fromEntity)

                // Domain → DTO
                .map(Product::toResponseDto)

                // Error controlado si no existe
                .orElseThrow(() -> new NotFoundException("Producto con id: " + id + " no encontrado"));
    }

    @Override
    public ProductsResponseDto partialUpdate(Long id, PartialUpdateProductsDto dto) {

        return productsRepo.findById(id)
                // Entity → Domain
                .map(Product::fromEntity)
                // Aplicar solo los cambios presentes
                .map(product -> product.partialUpdate(dto))

                // Domain → Entity
                .map(Product::toEntity)

                // Persistencia
                .map(productsRepo::save)

                // Entity → Domain
                .map(Product::fromEntity)

                // Domain → DTO
                .map(Product::toResponseDto)

                // Error si no existe
                .orElseThrow(() -> new NotFoundException("Producto con id: " + id + " no encontrado"));
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
}
