package com.product.shopping.repositories;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.product.shopping.entities.ShoppingCart;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ShoppingCartRepositoryTest {

	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	@Test
	public void testFindAll() {
		List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAll();
		assertThat(shoppingCarts.size(), is(greaterThan(0)));
	}
	
	@Test
	public void whenFindByExistingId_thenReturnShoppingCart() {
		Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(1L);
		assertThat(shoppingCart.isPresent(), is(true));
	}
	
	@Test
	public void whenFindByNonExistingId_thenReturnFalse() {
		Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(10L);
		assertThat(shoppingCart.isPresent(), is(false));
	}
}
