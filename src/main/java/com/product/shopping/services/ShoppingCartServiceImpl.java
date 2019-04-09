package com.product.shopping.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.product.shopping.entities.Product;
import com.product.shopping.entities.ProductCart;
import com.product.shopping.entities.ShoppingCart;
import com.product.shopping.exception.EmptyShoppingCartCreationException;
import com.product.shopping.exception.ProductQuantityExceedException;
import com.product.shopping.exception.ShoppingCartNotFoundException;
import com.product.shopping.model.ProductCartDto;
import com.product.shopping.model.ProductDto;
import com.product.shopping.model.ShoppingCartDto;
import com.product.shopping.repositories.ShoppingCartRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	private final ShoppingCartRepository shoppingCartRepository;

	public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
		this.shoppingCartRepository = shoppingCartRepository;
	}

	@Override
	public ShoppingCartDto saveShoppingCart(ShoppingCartDto shoppingCartDto) {
		ShoppingCart shoppingCart = new ShoppingCart();
		return saveShoppingCart(shoppingCart, shoppingCartDto);
	}

	@Override
	public ShoppingCartDto updateShoppingCart(ShoppingCartDto shoppingCartDto) {
		Optional<ShoppingCart> optional = shoppingCartRepository.findById(shoppingCartDto.getCartId());
		if (optional.isPresent()) {
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setCartId(shoppingCartDto.getCartId());
			return saveShoppingCart(shoppingCart, shoppingCartDto);
		}
		throw new ShoppingCartNotFoundException("Shopping cart ID " + shoppingCartDto.getCartId() + " not found.");
	}

	@Override
	public void deleteShoppingCart(Long shoppingCartId) {
		try {
			shoppingCartRepository.deleteById(shoppingCartId);
		} catch (Exception e) {
			throw new ShoppingCartNotFoundException("Shoppingcart with ID " + shoppingCartId + " does not found.");
		}

	}

	@Override
	public ShoppingCartDto getShoppingCart(Long shoppingCartId) {
		log.info("Get shoppingcart: " + shoppingCartId);
		Optional<ShoppingCart> optional = shoppingCartRepository.findById(shoppingCartId);
		if (optional.isPresent()) {
			ShoppingCart shoppingCart = optional.get();
			return getShoppingCartDto(shoppingCart);
		}
		throw new ShoppingCartNotFoundException("Shoppingcart ID " + shoppingCartId + " not found.");
	}

	@Override
	public List<ShoppingCartDto> getShoppingCarts() {
		log.info("Get all shopping carts.");
		return shoppingCartRepository.findAll().stream().map(this::getShoppingCartDto).collect(Collectors.toList());
	}

	private ShoppingCartDto saveShoppingCart(ShoppingCart shoppingCart, ShoppingCartDto shoppingCartDto) {
		if (shoppingCartDto.getProducts() != null) {
			shoppingCartDto.getProducts().stream().filter(productDto -> productDto.getQuantity() > 0)
					.forEach(productDto -> {
						ProductCart productCart = new ProductCart();

						Product product = new Product();
						product.setProductNumber(productDto.getProductDto().getProductNumber());

						productCart.setProduct(product);
						if (productDto.getQuantity() > 10) {
							throw new ProductQuantityExceedException(
									"Quantity exceeded for product: " + productDto.getProductDto().getProductNumber());
						}
						productCart.setQuantity(productDto.getQuantity());

						shoppingCart.addProductCart(productCart);
					});
			ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
			log.info("shoppingCart: " + savedShoppingCart);
			return getShoppingCartDto(savedShoppingCart);
		}

		throw new EmptyShoppingCartCreationException("Empty shopping cart creation error.");
	}

	private ShoppingCartDto getShoppingCartDto(ShoppingCart shoppingCart) {
		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
		shoppingCartDto.setCartId(shoppingCart.getCartId());

		List<ProductCartDto> products = shoppingCart.getProductCarts().stream().map(productCart -> {
			ProductCartDto productCartDto = new ProductCartDto();

			ProductDto productDto = new ProductDto();
			productDto.setProductNumber(productCart.getProduct().getProductNumber());
			productDto.setProductName(productCart.getProduct().getProductName());
			productCartDto.setProductDto(productDto);
			productCartDto.setQuantity(productCart.getQuantity());
			return productCartDto;
		}).collect(Collectors.toList());

		shoppingCartDto.setProducts(products);
		log.info("ShoppingCart: " + shoppingCartDto);
		return shoppingCartDto;
	}

}
