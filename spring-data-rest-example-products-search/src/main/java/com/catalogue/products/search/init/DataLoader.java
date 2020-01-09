package com.catalogue.products.search.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.catalogue.products.search.entity.Brand;
import com.catalogue.products.search.entity.Category;
import com.catalogue.products.search.entity.CategoryType;
import com.catalogue.products.search.entity.Color;
import com.catalogue.products.search.entity.Product;
import com.catalogue.products.search.entity.Size;
import com.catalogue.products.search.service.ProductsService;

/**
 * Initializes the database with sample data
 * @author Rajesh Borade
 *
 */
@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	private ProductsService productsService;
	
	@Override
	public void run(String... args) throws Exception {
		
		Brand adidas = new Brand("Adidas");
		Brand levi = new Brand("Levi");
		Brand armani = new Brand("Armani");
		Brand nike = new Brand("Nike");
		Brand zara = new Brand("Zara");
		
		Category shirt = new Category(CategoryType.SHIRT);
		Category cap = new Category(CategoryType.CAP);
		Category jeans = new Category(CategoryType.JEANS);
		
		Product shirt1 = new Product(shirt, Size.S, Color.BLACK, "SHIRT-S-BLK-ARMA", armani, 110);
		Product shirt2 = new Product(shirt, Size.L, Color.WHITE, "SHIRT-S-WHI-ZARA",  zara, 120);
		Product shirt3 = new Product(shirt, Size.M, Color.YELLOW, "SHIRT-S-YEL-NIKE", nike, 130);
		
		Product cap1 = new Product(cap, Size.S, Color.GREEN, "CAP-S-GRE-NIKE", nike, 10);
		Product cap2 = new Product(cap, Size.M, Color.RED, "CAP-M-RED-ADID", adidas, 10);
		
		Product jeans1 = new Product(jeans, Size.L, Color.BLUE, "JNS-L-BLU-LEVI", levi, 200);
		Product jeans2 = new Product(jeans, Size.XL, Color.BLACK, "JNS-XL-BLK-ARMA", armani, 250);
		
		productsService.save(shirt1);
		productsService.save(shirt2);
		productsService.save(shirt3);
		productsService.save(cap1);
		productsService.save(cap2);		
		productsService.save(jeans1);
		productsService.save(jeans2);
	}

}
