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

    @Query(value = "SELECT DISTINCT p.* FROM products p " +
           "LEFT JOIN product_categories pc ON p.id = pc.product_id " +
           "WHERE p.owner_id = :userId " +
           "AND (:name::text IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:categoryId IS NULL OR pc.category_id = :categoryId)",
           nativeQuery = true)
    List<ProductsEntity> findByUserIdWithFilters(
        @Param("userId") Long userId,
        @Param("name") String name,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("categoryId") Long categoryId
    );

}