package com.product.shopping.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.product.shopping.exception.EmptyShoppingCartCreationException;
import com.product.shopping.exception.ShoppingCartNotFoundException;
import com.product.shopping.model.ShoppingCartDto;
import com.product.shopping.services.ShoppingCartService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(produces = "application/json")
public class ShoppingCartController {

	private final ShoppingCartService shoppingCartService;

	@Autowired
	public ShoppingCartController(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}

	@GetMapping("/shoppingcart")
	public ResponseEntity<List<ShoppingCartDto>> getShoppingCarts() {
		List<ShoppingCartDto> shoppingCartList = shoppingCartService.getShoppingCarts();
		if (!shoppingCartList.isEmpty()) {
			return new ResponseEntity<>(shoppingCartList, HttpStatus.OK);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shopping cart found.");

	}

	@GetMapping("/shoppingcart/{cartid}")
	public ResponseEntity<ShoppingCartDto> getShoppingCart(@PathVariable(name = "cartid") Long cartid) {
		log.debug("cartid: " + cartid);
		try {
			ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(cartid);
			return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
		} catch (ShoppingCartNotFoundException scnfe) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, scnfe.getMessage());
		}

	}

	@PostMapping("/shoppingcart")
	public ResponseEntity<ShoppingCartDto> createShoppingCart(@RequestBody ShoppingCartDto shoppingCartDto) {
		try {
			return new ResponseEntity<>(shoppingCartService.saveShoppingCart(shoppingCartDto), HttpStatus.CREATED);
		} catch (ShoppingCartNotFoundException | EmptyShoppingCartCreationException ce) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ce.getMessage());
		}
	}

	@PutMapping("/shoppingcart/{cartid}")
	public ResponseEntity<ShoppingCartDto> updateShoppingCart(
			@PathVariable(name = "cartid", required = true) Long cartid, @RequestBody ShoppingCartDto shoppingCartDto) {
		try {
			shoppingCartDto.setCartId(cartid);
			return new ResponseEntity<>(shoppingCartService.updateShoppingCart(shoppingCartDto), HttpStatus.OK);
		} catch (ShoppingCartNotFoundException | EmptyShoppingCartCreationException ce) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ce.getMessage());
		}
	}

	@DeleteMapping("/shoppingcart/{cartid}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteShoppingCart(@PathVariable(name = "cartid") Long cartid) {
		try {
			shoppingCartService.deleteShoppingCart(cartid);
			return ResponseEntity.noContent().build();
		} catch (ShoppingCartNotFoundException ce) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ce.getMessage());
		}

	}

}
