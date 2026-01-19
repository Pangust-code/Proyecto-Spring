package ec.edu.ups.icc.fundamentos01.users.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductsEntity;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    @Query(value = "SELECT DISTINCT p.* FROM products p " +
           "LEFT JOIN product_categories pc ON p.id = pc.product_id " +
           "WHERE p.owner_id = :userId " +
           "AND (CAST(:name AS TEXT) IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', CAST(:name AS TEXT), '%'))) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:categoryId IS NULL OR pc.category_id = :categoryId)",
           nativeQuery = true)
    List<ProductsEntity> findProductsByUserIdWithFilters(
        @Param("userId") Long userId,
        @Param("name") String name,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice,
        @Param("categoryId") Long categoryId
    );

}
