package com.product.shopping.exception;

public class EmptyShoppingCartCreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmptyShoppingCartCreationException(String msg) {
		super(msg);
	}

}
