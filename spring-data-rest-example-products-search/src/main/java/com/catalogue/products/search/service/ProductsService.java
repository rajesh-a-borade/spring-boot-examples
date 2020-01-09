package com.catalogue.products.search.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catalogue.products.search.entity.Brand;
import com.catalogue.products.search.entity.Category;
import com.catalogue.products.search.entity.Color;
import com.catalogue.products.search.entity.Product;
import com.catalogue.products.search.entity.Size;
import com.catalogue.products.search.groupby.ProductsGroupedBy;
import com.catalogue.products.search.repo.BrandRepository;
import com.catalogue.products.search.repo.CategoryRepository;
import com.catalogue.products.search.repo.ProductsRepository;

/**
 * @author Rajesh Borade
 */
public interface ProductsService {
	public Product save(Product product);
	public List<ProductsGroupedBy<Color>> groupByColor();
	public List<ProductsGroupedBy<Brand>> groupByBrand();
	public List<ProductsGroupedBy<Size>> groupBySize();
}

/**
 * @author Rajesh Borade
 */
@Service
class ProductServiceImpl implements ProductsService {

	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private ProductsRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	@Transactional
	public Product save(Product product) {
		
		Optional<Brand> brand = brandRepository.findByName(product.getBrand().getName());
		if(!brand.isPresent()) {
			brandRepository.save(product.getBrand());
		}
		Optional<Category> category = categoryRepository.findByType(product.getCategory().getType());
		if(!category.isPresent()) {
			categoryRepository.save(product.getCategory());
		}
		return productRepository.save(product);
	}
	
	public List<ProductsGroupedBy<Color>> groupByColor() {
		return productRepository.groupByColor();
	}
	
	public List<ProductsGroupedBy<Brand>> groupByBrand() {
		return productRepository.groupByBrand();
	}

	@Override
	public List<ProductsGroupedBy<Size>> groupBySize() {
		return productRepository.groupBySize();
	}
}