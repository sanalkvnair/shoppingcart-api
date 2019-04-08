package com.product.shopping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.product.shopping.entities.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

}
