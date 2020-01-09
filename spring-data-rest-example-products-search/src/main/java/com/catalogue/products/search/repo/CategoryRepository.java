package com.catalogue.products.search.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.catalogue.products.search.entity.Category;
import com.catalogue.products.search.entity.CategoryType;

/**
 * @author Rajesh Borade
 */
public interface CategoryRepository extends JpaRepository<Category, Long>{

	public Optional<Category> findByType(@Param("type") CategoryType type);
}
