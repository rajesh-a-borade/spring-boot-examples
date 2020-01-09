package com.catalogue.products.search.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.catalogue.products.search.entity.Brand;
import com.catalogue.products.search.entity.Color;
import com.catalogue.products.search.entity.Product;
import com.catalogue.products.search.entity.Size;
import com.catalogue.products.search.groupby.ProductsGroupedBy;

/**
 * @author Rajesh Borade
 */
@RepositoryRestResource
public interface ProductsRepository extends JpaRepository<Product, Long> {
	
	
	@Query("select new com.catalogue.products.search.groupby.ProductsGroupedBy(p.color as color, "
			+ "min(p.price) as minPrice, "
			+ "max(p.price) as maxPrice, "
			+ "count(p.id) as countProduct, "
			+ "avg(p.price) as avgPrice) "
			+ "from Product p "
			+ "group by p.color")
	public List<ProductsGroupedBy<Color>> groupByColor();

	@Query("select new com.catalogue.products.search.groupby.ProductsGroupedBy(p.brand as brand, "
			+ "min(p.price) as minPrice, "
			+ "max(p.price) as maxPrice, "
			+ "count(p.id) as countProduct, "
			+ "avg(p.price) as avgPrice) "
			+ "from Product p "
			+ "group by p.brand")
	public List<ProductsGroupedBy<Brand>> groupByBrand();
	
	@Query("select new com.catalogue.products.search.groupby.ProductsGroupedBy(p.size as size, "
			+ "min(p.price) as minPrice, "
			+ "max(p.price) as maxPrice, "
			+ "count(p.id) as countProduct, "
			+ "avg(p.price) as avgPrice) "
			+ "from Product p "
			+ "group by p.size")
	public List<ProductsGroupedBy<Size>> groupBySize();
}
