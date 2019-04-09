package com.product.shopping.services;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.product.shopping.entities.ShoppingCart;
import com.product.shopping.exception.EmptyShoppingCartCreationException;
import com.product.shopping.exception.ProductQuantityExceedException;
import com.product.shopping.exception.ShoppingCartNotFoundException;
import com.product.shopping.model.ProductCartDto;
import com.product.shopping.model.ProductDto;
import com.product.shopping.model.ShoppingCartDto;
import com.product.shopping.repositories.ShoppingCartRepository;

@RunWith(SpringRunner.class)
public class ShoppingCartServiceTest {

	private ShoppingCartService shoppingCartService;

	@Mock
	private ShoppingCartRepository shoppingCartRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		shoppingCartService = new ShoppingCartServiceImpl(shoppingCartRepository);
	}

	@Test
	public void testSaveShoppingCart_ReturnsCartId() {
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCartId(100L);
		when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
		ProductDto productDto = new ProductDto();
		productDto.setProductNumber("123-123-01");
		ProductCartDto productCartDto = new ProductCartDto();
		productCartDto.setProductDto(productDto);
		productCartDto.setQuantity(1);
		List<ProductCartDto> products = new ArrayList<>();
		products.add(productCartDto);
		shoppingCartDto.setProducts(products);
		ShoppingCartDto savedShoppingCart = shoppingCartService.saveShoppingCart(shoppingCartDto);
		assertThat(savedShoppingCart.getCartId()).isNotNull();
	}

	@Test
	public void testSaveShoppingCart_CalledOnlyOnce() {
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCartId(100L);
		when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
		ProductDto productDto = new ProductDto();
		productDto.setProductNumber("123-123-01");
		ProductCartDto productCartDto = new ProductCartDto();
		productCartDto.setProductDto(productDto);
		productCartDto.setQuantity(1);
		List<ProductCartDto> products = new ArrayList<>();
		products.add(productCartDto);
		shoppingCartDto.setProducts(products);
		shoppingCartService.saveShoppingCart(shoppingCartDto);
		verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
	}

	@Test
	public void testUpdateExistingShoppingCart_ReturnSameShoppingCart() {
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCartId(100L);
		when(shoppingCartRepository.findById(100L)).thenReturn(Optional.of(shoppingCart));
		when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);

		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
		shoppingCartDto.setCartId(100L);
		ProductDto productDto = new ProductDto();
		productDto.setProductNumber("123-123-01");
		ProductCartDto productCartDto = new ProductCartDto();
		productCartDto.setProductDto(productDto);
		productCartDto.setQuantity(1);
		List<ProductCartDto> products = new ArrayList<>();
		products.add(productCartDto);
		shoppingCartDto.setProducts(products);
		ShoppingCartDto updatedShoppingCart = shoppingCartService.updateShoppingCart(shoppingCartDto);
		assertThat(updatedShoppingCart.getCartId()).isEqualTo(shoppingCart.getCartId());
	}

	@Test
	public void testUpdateNonExistingShoppingCart_ThrowError() {
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCartId(100L);
		when(shoppingCartRepository.findById(1L)).thenReturn(Optional.empty());
		when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);

		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
		shoppingCartDto.setCartId(1L);
		ProductDto productDto = new ProductDto();
		productDto.setProductNumber("123-123-01");
		ProductCartDto productCartDto = new ProductCartDto();
		productCartDto.setProductDto(productDto);
		productCartDto.setQuantity(1);
		List<ProductCartDto> products = new ArrayList<>();
		products.add(productCartDto);
		shoppingCartDto.setProducts(products);
		assertThatThrownBy(() -> shoppingCartService.updateShoppingCart(shoppingCartDto))
				.isInstanceOf(ShoppingCartNotFoundException.class)
				.hasMessage("Shopping cart ID " + shoppingCartDto.getCartId() + " not found.");
	}

	@Test
	public void testSaveShoppingCart_ThrowQuatityExceedError() {
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCartId(100L);
		when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
		ProductDto productDto = new ProductDto();
		productDto.setProductNumber("123-123-01");
		ProductCartDto productCartDto = new ProductCartDto();
		productCartDto.setProductDto(productDto);
		productCartDto.setQuantity(11);
		List<ProductCartDto> products = new ArrayList<>();
		products.add(productCartDto);
		shoppingCartDto.setProducts(products);
		assertThatThrownBy(() -> shoppingCartService.saveShoppingCart(shoppingCartDto))
				.isInstanceOf(ProductQuantityExceedException.class)
				.hasMessage("Quantity exceeded for product: " + productDto.getProductNumber());
	}

	@Test
	public void testSaveEmptyShoppingCart_ThrowEmptyShoppingCartError() {
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCartId(100L);
		when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
		assertThatThrownBy(() -> shoppingCartService.saveShoppingCart(shoppingCartDto))
				.isInstanceOf(EmptyShoppingCartCreationException.class)
				.hasMessage("Empty shopping cart creation error.");
	}
}
