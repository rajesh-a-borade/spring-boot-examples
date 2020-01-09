package com.catalogue.products.search.groupby;

/**
 * @author Rajesh Borade
 */
public class ProductsGroupedBy<T> {

	private T group;
	private Double minPrice;
	private Double maxPrice;
	private Long countProduct;
	private Double avgPrice;

	public T getGroup() {
		return group;
	}

	public void setGroup(T t) {
		this.group = t;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Long getCountProduct() {
		return countProduct;
	}

	public void setCountProduct(Long countProduct) {
		this.countProduct = countProduct;
	}

	public Double getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(Double avgPrice) {
		this.avgPrice = avgPrice;
	}

	public ProductsGroupedBy(T t, Double minPrice, Double maxPrice, Long countProduct, Double avgPrice) {
		this.group = t;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.countProduct = countProduct;
		this.avgPrice = avgPrice;
	}

	public ProductsGroupedBy() {
	}

	@Override
	public String toString() {
		return "ProductsGroupByColour [" + group + ", minPrice=" + minPrice + ", maxPrice=" + maxPrice
				+ ", countProduct=" + countProduct + ", avgPrice=" + avgPrice + "]";
	}
	
	

}