package com.product.shopping.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="SHOPPING_CART")
public class ShoppingCart {

	@Setter
	@Getter
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CART_ID")
	private Long cartId;
	
	@Getter
	@OneToMany(mappedBy="shoppingCart", cascade=CascadeType.ALL, orphanRemoval=true)
	private Set<ProductCart> productCarts = new HashSet<>();
	
	public void addProductCart(ProductCart productCart) {
		productCarts.add(productCart);
		productCart.setShoppingCart(this);
	}
	
	public void removeProductCart(ProductCart productCart) {
		productCarts.remove(productCart);
		productCart.setShoppingCart(null);
	}
}
