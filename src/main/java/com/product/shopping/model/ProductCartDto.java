package com.product.shopping.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCartDto {

	private Integer quantity;
	@JsonProperty("product")
	private ProductDto productDto;
}
