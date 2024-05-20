package com.kamatchibotique.application.service.services.order.orderstatushistory;

import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.model.order.orderstatus.OrderStatusHistory;

import java.util.List;

public interface OrderStatusHistoryService {
    List<OrderStatusHistory> findByOrder(Order order);
}
