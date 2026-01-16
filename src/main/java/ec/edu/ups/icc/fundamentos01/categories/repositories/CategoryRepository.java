package ec.edu.ups.icc.fundamentos01.categories.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ec.edu.ups.icc.fundamentos01.categories.entities.CategoryEntity;



public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>{

    boolean existsByName(String name);

    Optional<CategoryEntity> findByName(String name);
    
}
