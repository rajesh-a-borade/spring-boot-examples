package com.catalogue.products.search.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.catalogue.products.search.entity.Brand;
import com.catalogue.products.search.entity.Color;
import com.catalogue.products.search.entity.Size;
import com.catalogue.products.search.groupby.ProductsGroupedBy;
import com.catalogue.products.search.service.ProductsService;

/**
 * @author Rajesh Borade
 */
@RestController
@RequestMapping("/search")
public class SearchController {
	
	@Autowired
	private ProductsService productsService;

	@GetMapping("/groupBy/color")
	public @ResponseBody List<ProductsGroupedBy<Color>> groupByColor() {
		return productsService.groupByColor();
	}
	
	@GetMapping("/groupBy/brand")
	public @ResponseBody List<ProductsGroupedBy<Brand>> groupByBrand() {
		return productsService.groupByBrand();
	}
	
	@GetMapping("/groupBy/size")
	public @ResponseBody List<ProductsGroupedBy<Size>> groupBySize() {
		return productsService.groupBySize();
	}
}
