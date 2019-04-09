package com.product.shopping.exception;

public class ProductQuantityExceedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProductQuantityExceedException(String msg) {
		super(msg);
	}
}
