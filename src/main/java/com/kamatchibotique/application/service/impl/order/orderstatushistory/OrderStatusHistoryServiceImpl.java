package com.kamatchibotique.application.service.impl.order.orderstatushistory;

import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.model.order.orderstatus.OrderStatusHistory;
import com.kamatchibotique.application.repository.order.OrderStatusHistoryRepository;
import com.kamatchibotique.application.service.services.order.orderstatushistory.OrderStatusHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderStatusHistoryServiceImpl implements OrderStatusHistoryService {
    @Autowired
    private OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Override
    public List<OrderStatusHistory> findByOrder(Order order) {
        return orderStatusHistoryRepository.findByOrderId(order.getId());
    }
}
