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
import com.product.shopping.exception.ProductQuantityExceedException;
import com.product.shopping.exception.ShoppingCartNotFoundException;
import com.product.shopping.model.ShoppingCartDto;
import com.product.shopping.services.ShoppingCartService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(produces = "application/json")
@Api(value = "Shopping Cart Backend", produces = "application/json")
public class ShoppingCartController {

	private final ShoppingCartService shoppingCartService;

	@Autowired
	public ShoppingCartController(ShoppingCartService shoppingCartService) {
		this.shoppingCartService = shoppingCartService;
	}

	@ApiOperation(value = "Get all shopping carts", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "No shopping cart found.") })
	@GetMapping("/shoppingcart")
	public ResponseEntity<List<ShoppingCartDto>> getShoppingCarts() {
		List<ShoppingCartDto> shoppingCartList = shoppingCartService.getShoppingCarts();
		if (!shoppingCartList.isEmpty()) {
			return new ResponseEntity<>(shoppingCartList, HttpStatus.OK);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shopping cart found.");

	}

	@ApiOperation(value = "Get shopping cart by cart id", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Shopping cart ID not found") })
	@GetMapping("/shoppingcart/{cartid}")
	public ResponseEntity<ShoppingCartDto> getShoppingCart(
			@ApiParam(value = "ShoppingCart ID for fetching Shopping cart", example = "1") @PathVariable(name = "cartid") Long cartid) {
		log.debug("cartid: " + cartid);
		try {
			ShoppingCartDto shoppingCartDto = shoppingCartService.getShoppingCart(cartid);
			return new ResponseEntity<>(shoppingCartDto, HttpStatus.OK);
		} catch (ShoppingCartNotFoundException scnfe) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, scnfe.getMessage());
		}

	}

	@ApiOperation(value = "Save shopping cart", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "CREATED"),
			@ApiResponse(code = 404, message = "Empty Shopping cart creation"),
			@ApiResponse(code = 404, message = "Product quantity exceed exception") })
	@PostMapping("/shoppingcart")
	public ResponseEntity<ShoppingCartDto> createShoppingCart(
			@ApiParam(value = "ShoppingCart object to store in database", required = true) @RequestBody(required = true) ShoppingCartDto shoppingCartDto) {
		try {
			return new ResponseEntity<>(shoppingCartService.saveShoppingCart(shoppingCartDto), HttpStatus.CREATED);
		} catch (EmptyShoppingCartCreationException | ProductQuantityExceedException ce) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ce.getMessage());
		}
	}

	@ApiOperation(value = "Update shopping cart", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 404, message = "Shopping cart ID not found"),
			@ApiResponse(code = 404, message = "Empty Shopping cart creation"),
			@ApiResponse(code = 404, message = "Product quantity exceed exception") })
	@PutMapping("/shoppingcart/{cartid}")
	public ResponseEntity<ShoppingCartDto> updateShoppingCart(
			@ApiParam(value = "ShoppingCart ID to be updated", required = true, example = "1") @PathVariable(name = "cartid", required = true) Long cartid,
			@ApiParam(value = "ShoppingCart object to store in database", required = true) @RequestBody(required = true) ShoppingCartDto shoppingCartDto) {
		try {
			shoppingCartDto.setCartId(cartid);
			return new ResponseEntity<>(shoppingCartService.updateShoppingCart(shoppingCartDto), HttpStatus.OK);
		} catch (ShoppingCartNotFoundException | EmptyShoppingCartCreationException
				| ProductQuantityExceedException ce) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ce.getMessage());
		}
	}

	@ApiOperation(value = "Delete shopping cart", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 204, message = "NO_CONTENT"),
			@ApiResponse(code = 404, message = "Shopping cart ID not found") })
	@DeleteMapping("/shoppingcart/{cartid}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteShoppingCart(
			@ApiParam(value = "ShoppingCart ID to be deleted", required = true, example = "1") @PathVariable(name = "cartid", required = true) Long cartid) {
		try {
			shoppingCartService.deleteShoppingCart(cartid);
			return ResponseEntity.noContent().build();
		} catch (ShoppingCartNotFoundException ce) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, ce.getMessage());
		}

	}

}
