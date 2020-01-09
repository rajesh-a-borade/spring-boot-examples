package com.catalogue.products.search.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * @author Rajesh Borade
 */
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToOne
	@JoinColumn(name = "category_id")
    private Category category;
	
	@Enumerated(EnumType.STRING)
	private Size size;
	
	@Enumerated(EnumType.STRING)
	private Color color;
	
	private String sku;

	@OneToOne
	@JoinColumn(name = "brand_id")
    private Brand brand;
	
	private double price;
	
	public Product() {
	}
	
	public Product(Category category, Size size, Color color, String sku, Brand brand, double price) {
		this.category = category;
		this.size = size;
		this.color = color;
		this.sku = sku;
		this.brand = brand;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", category=" + category + ", size=" + size + ", color=" + color + ", sku=" + sku
				+ ", brand=" + brand + ", price=" + price + "]";
	}

	
}
