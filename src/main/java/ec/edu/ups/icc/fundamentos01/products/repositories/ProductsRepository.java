package ec.edu.ups.icc.fundamentos01.products.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
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

    List<ProductsEntity> findByCategoryId(Long id);

    List<ProductsEntity> findByOwnerName(String name);

    List<ProductsEntity> findByCategoryName(String name);

    List<ProductsEntity> findByCategoryIdAndPriceGreaterThan(Long categoryId, Double price);



}