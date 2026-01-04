package ec.edu.ups.icc.fundamentos01.products.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ec.edu.ups.icc.fundamentos01.products.entities.ProductsEntity;

@Repository
public interface ProductsRepository extends JpaRepository<ProductsEntity, Long> {

    Optional<ProductsEntity> findByName(String name);

}