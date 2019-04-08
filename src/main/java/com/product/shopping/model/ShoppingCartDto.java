package com.product.shopping.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShoppingCartDto {

	private Long cartId;
	private List<ProductCartDto> products;
}
