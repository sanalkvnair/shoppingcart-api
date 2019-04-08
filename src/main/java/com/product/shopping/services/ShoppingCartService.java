package com.product.shopping.services;

import java.util.List;

import com.product.shopping.model.ShoppingCartDto;

public interface ShoppingCartService {

	ShoppingCartDto saveShoppingCart(ShoppingCartDto shoppingCartDto);
	ShoppingCartDto updateShoppingCart(ShoppingCartDto shoppingCartDto);
	void deleteShoppingCart(Long shoppingCartId);
	ShoppingCartDto getShoppingCart(Long shoppingCartId);
	List<ShoppingCartDto> getShoppingCarts();
}
