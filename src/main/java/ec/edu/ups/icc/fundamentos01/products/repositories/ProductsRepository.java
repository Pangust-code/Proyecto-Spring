package ec.edu.ups.icc.fundamentos01.products.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductsEntity;
import java.util.List;


@Repository
public interface ProductsRepository extends JpaRepository<ProductsEntity, Long> {

    java.util.List<ProductsEntity> findByNameContainingIgnoreCase(String name);

    Optional<ProductsEntity> findByName(String name);

    java.util.List<ProductsEntity> findByPriceLessThan(Double price);

    java.util.List<ProductsEntity> findByStockGreaterThan(Integer stock);

    List<ProductsEntity> findByOwnerId(Long id);

    List<ProductsEntity> findByCategories_Id(Long id);

    List<ProductsEntity> findByOwnerName(String name);

    List<ProductsEntity> findByCategories_Name(String name);

    List<ProductsEntity> findByCategories_IdAndPriceGreaterThan(Long categoryId, Double price);

    // Métodos con fetch join para cargar las relaciones LAZY
    @Query("SELECT DISTINCT p FROM ProductsEntity p LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.categories")
    List<ProductsEntity> findAllWithRelations();

    @Query("SELECT p FROM ProductsEntity p LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.categories WHERE p.id = :id")
    Optional<ProductsEntity> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM ProductsEntity p LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.categories WHERE p.owner.id = :userId")
    List<ProductsEntity> findByOwnerIdWithRelations(@Param("userId") Long userId);

    @Query("SELECT DISTINCT p FROM ProductsEntity p LEFT JOIN FETCH p.owner LEFT JOIN FETCH p.categories c WHERE c.id = :categoryId")
    List<ProductsEntity> findByCategoryIdWithRelations(@Param("categoryId") Long categoryId);

    /**
     * Busca productos de un usuario específico aplicando filtros opcionales.
     * Todos los filtros son opcionales (pueden ser null).
     * Los filtros se aplican a nivel de base de datos para optimizar el rendimiento.
     * 
     * @param userId ID del usuario propietario (obligatorio)
     * @param name Nombre del producto (opcional, búsqueda parcial case-insensitive)
     * @param minPrice Precio mínimo (opcional, inclusive)
     * @param maxPrice Precio máximo (opcional, inclusive)
     * @param categoryId ID de categoría (opcional)
     * @return Lista de productos que cumplen los criterios con relaciones cargadas
     */
    @Query("SELECT DISTINCT p FROM ProductsEntity p " +
           "LEFT JOIN FETCH p.owner " +
           "LEFT JOIN FETCH p.categories c " +
           "WHERE p.owner.id = :userId " +
           "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:categoryId IS NULL OR c.id = :categoryId)")
    List<ProductsEntity> findByUserIdWithFilters(
        @Param("userId") Long userId,
        @Param("name") String name,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("categoryId") Long categoryId
    );

}