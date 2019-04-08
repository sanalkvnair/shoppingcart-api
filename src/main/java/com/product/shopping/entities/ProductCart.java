package com.product.shopping.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="PRODUCT_CART")
public class ProductCart {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="PRODUCT_CART_ID")
	private Long productCartId;
	
	@Column(name="QUANTITY")
	private Integer quantity;
	
	@ManyToOne
	@JoinColumn(name="PRODUCT_NUMBER", nullable=false, referencedColumnName="PRODUCT_NUMBER")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="SHOPPING_CART_ID", nullable=false, referencedColumnName="CART_ID")
	private ShoppingCart shoppingCart;
}
