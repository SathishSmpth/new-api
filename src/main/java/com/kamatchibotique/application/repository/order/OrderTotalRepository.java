package com.kamatchibotique.application.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamatchibotique.application.model.order.OrderTotal;

public interface OrderTotalRepository extends JpaRepository<OrderTotal, Long> {


}
