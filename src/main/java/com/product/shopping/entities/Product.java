package com.product.shopping.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="PRODUCT")
public class Product {

	@Id
	@Column(name = "PRODUCT_NUMBER")
	private String productNumber;
	
	@Column(name="PRODUCT_NAME", nullable=false)
	private String productName;
	
	@OneToMany(mappedBy="product")
	private List<ProductCart> productCarts;
}
