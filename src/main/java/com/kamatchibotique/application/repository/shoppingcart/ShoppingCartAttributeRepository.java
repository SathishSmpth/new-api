package com.kamatchibotique.application.repository.shoppingcart;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamatchibotique.application.model.shoppingcart.ShoppingCartAttributeItem;
public interface ShoppingCartAttributeRepository extends JpaRepository<ShoppingCartAttributeItem, Long> {


}
