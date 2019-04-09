package com.product.shopping.controller;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import com.product.shopping.model.ShoppingCartDto;
import com.product.shopping.services.ShoppingCartService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ShoppingCartService shoppingCartService;

	@Test
	public void testGetAllShoppingCart_thenReturnJsonArray() throws Exception {
		ShoppingCartDto shoppingCart = new ShoppingCartDto();
		shoppingCart.setCartId(1L);

		List<ShoppingCartDto> shoppingCarts = Arrays.asList(shoppingCart);

		when(shoppingCartService.getShoppingCarts()).thenReturn(shoppingCarts);

		mvc.perform(get("/shoppingcart").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$", hasSize(1)));
	}
	
	@Test
	public void testGetAllShoppingCart_thenReturnShoppingCartJson() throws Exception {
		ShoppingCartDto shoppingCart = new ShoppingCartDto();
		shoppingCart.setCartId(1L);

		when(shoppingCartService.getShoppingCart(1L)).thenReturn(shoppingCart);

		mvc.perform(get("/shoppingcart/1").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.cartId", is(1)));
	}
}
