package com.catalogue.products.search.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.catalogue.products.search.entity.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long> {

	public Optional<Brand> findByName(@Param("name") String name);
}
