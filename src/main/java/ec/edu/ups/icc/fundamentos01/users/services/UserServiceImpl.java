package ec.edu.ups.icc.fundamentos01.users.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.edu.ups.icc.fundamentos01.categories.dtos.CategoriaResponseDto;
import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;

import ec.edu.ups.icc.fundamentos01.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductsResponseDto;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductsMapper;
import ec.edu.ups.icc.fundamentos01.products.repositories.ProductsRepository;
import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.User;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final ProductsRepository productRepo;

    public UserServiceImpl(UserRepository userRepo, ProductsRepository productRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
    }

    // Actualizar código de los métodos para usar userRepo
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
    public List<UserResponseDto> findAll() {

        // Lista final que se devolverá al controlador
        List<UserResponseDto> response = new ArrayList<>();

        // 1. Obtener todas las entidades desde la base de datos
        List<UserEntity> entities = userRepo.findAll();

        // 2. Iterar sobre cada entidad
        for (UserEntity entity : entities) {

            // 3. Convertir la entidad en modelo de dominio
            User user = User.fromEntity(entity);

            // 4. Convertir el modelo de dominio en DTO de respuesta
            UserResponseDto dto = UserMapper.toResponse(user);

            // 5. Agregar el DTO a la lista de resultados
            response.add(dto);
        }

        // 6. Retornar la lista final de DTOs
        return response;
    }

    @Override
    public UserResponseDto findOne(int id) {
        return userRepo.findById((long) id)
                .map(User::fromEntity)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Usuario con id: " + id + " no encontrado"));
    }

    @Override
    public UserResponseDto create(CreateUserDto dto) {
        // Validar que el email no exista ya ANTES de intentar insertar
        if (userRepo.findByEmail(dto.email()).isPresent()) {
            throw new ConflictException("El email '" + dto.email() + "' ya está registrado");
        }
        
        return Optional.of(dto)
                .map(UserMapper::fromCreateDto)
                .map(User::toEntity)
                .map(userRepo::save)
                .map(User::fromEntity)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new ConflictException("Error al crear el usuario" + dto));
    }

    @Override
    public UserResponseDto update(int id, UpdateUserDto dto) {

        return userRepo.findById((long) id)
                // Entity → Domain
                .map(User::fromEntity)

                // Aplicar cambios permitidos en el dominio
                .map(user -> user.update(dto))

                // Domain → Entity
                .map(User::toEntity)

                // Persistencia
                .map(userRepo::save)

                // Entity → Domain
                .map(User::fromEntity)

                // Domain → DTO
                .map(UserMapper::toResponse)

                // Error controlado si no existe
                .orElseThrow(() -> new NotFoundException("Usuario con id: " + id + " no encontrado"));
    }

    @Override
    public UserResponseDto partialUpdate(int id, PartialUpdateUserDto dto) {

        return userRepo.findById((long) id)
                // Entity → Domain
                .map(User::fromEntity)

                // Aplicar solo los cambios presentes
                .map(user -> user.partialUpdate(dto))

                // Domain → Entity
                .map(User::toEntity)

                // Persistencia
                .map(userRepo::save)

                // Entity → Domain
                .map(User::fromEntity)

                // Domain → DTO
                .map(UserMapper::toResponse)

                // Error si no existe
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id: " + id));
    }

   @Override
    public void delete(int id) {
          // Verifica existencia y elimina
        userRepo.findById((long) id)
        .ifPresentOrElse(
            userRepo::delete,
            () -> {
                throw new NotFoundException("Usuario con id: " + id + " no encontrado");
            }
        );
    }

    @Override
    public List<ProductsResponseDto> getProductsByUserId(Long userId) {
        // Consulta productos por ownerId usando ProductRepository.
        userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));

        return productRepo.findByOwnerId(userId)
                .stream()
                .map(ProductsMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductsResponseDto> getProductsByUserIdWithFilters(
            Long userId,
            String name,
            Double minPrice,
            Double maxPrice,
            Long categoryId) {

        // Verificar que el usuario existe
        userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + userId));

        // Consultar productos desde UserRepository aplicando filtros en base de datos
        return userRepo.findProductsByUserIdWithFilters(userId, name, minPrice, maxPrice, categoryId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    /**
     * Mapea ProductsEntity a ProductsResponseDto cargando relaciones LAZY dentro de la transacción
     */
    private ProductsResponseDto toResponseDto(ec.edu.ups.icc.fundamentos01.products.entities.ProductsEntity entity) {
        ProductsResponseDto dto = new ProductsResponseDto();

        // Campos básicos
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.price = entity.getPrice();
        dto.stock = entity.getStock();
        dto.createdAt = entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null;
        dto.updatedAt = entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null;

        // Mapear usuario si existe (forzar carga LAZY)
        if (entity.getOwner() != null) {
            ProductsResponseDto.UserSummaryDto userDto = new ProductsResponseDto.UserSummaryDto();
            userDto.id = entity.getOwner().getId();
            userDto.name = entity.getOwner().getName();
            userDto.email = entity.getOwner().getEmail();
            dto.userId = userDto;
        } else {
            dto.userId = null;
        }

        // Mapear categorías si existen (forzar carga LAZY)
        if (entity.getCategories() != null && !entity.getCategories().isEmpty()) {
            dto.categories = entity.getCategories().stream()
                    .map(this::toCategoryResponseDto)
                    .sorted((left, right) -> left.name.compareToIgnoreCase(right.name))
                    .toList();
        } else {
            dto.categories = null;
        }

        return dto;
    }

    private CategoriaResponseDto toCategoryResponseDto(CategoryEntity category) {
        CategoriaResponseDto dto = new CategoriaResponseDto();
        dto.id = category.getId();
        dto.name = category.getName();
        dto.description = category.getDescription();
        return dto;
    }

}
