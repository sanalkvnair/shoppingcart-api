package com.product.shopping.exception;

public class ShoppingCartNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ShoppingCartNotFoundException(String exception) {
		super(exception);
	}
}
