package com.kamatchibotique.application.repository.order.orderproduct;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamatchibotique.application.model.order.orderproduct.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {


}
