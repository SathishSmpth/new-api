package com.kamatchibotique.application.repository.order.orderaccount;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kamatchibotique.application.model.order.orderaccount.OrderAccount;

public interface OrderAccountRepository extends JpaRepository<OrderAccount, Long> {


}
