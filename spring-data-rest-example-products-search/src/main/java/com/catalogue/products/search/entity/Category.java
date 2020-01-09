package com.catalogue.products.search.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Rajesh Borade
 */
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Enumerated(EnumType.STRING)
	private CategoryType type;
	
	public Category() {
	}

	public Category(CategoryType type) {
		this.type = type;
	}

	public CategoryType getType() {
		return type;
	}
	
	public void setType(CategoryType type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}

	
	@Override
	public String toString() {
		return "Category [id=" + id + ", type=" + type + "]";
	}

}
