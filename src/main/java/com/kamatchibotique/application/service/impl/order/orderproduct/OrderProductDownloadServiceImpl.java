package com.kamatchibotique.application.service.impl.order.orderproduct;

import com.kamatchibotique.application.model.order.orderproduct.OrderProductDownload;
import com.kamatchibotique.application.repository.order.orderproduct.OrderProductDownloadRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.order.orderproduct.OrderProductDownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;




@Service("orderProductDownloadService")
public class OrderProductDownloadServiceImpl  extends CommonServiceImpl<Long, OrderProductDownload> implements OrderProductDownloadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProductDownloadServiceImpl.class);


    private final OrderProductDownloadRepository orderProductDownloadRepository;

   @Autowired
    public OrderProductDownloadServiceImpl(OrderProductDownloadRepository orderProductDownloadRepository) {
        super(orderProductDownloadRepository);
        this.orderProductDownloadRepository = orderProductDownloadRepository;
    }
    
    @Override
    public List<OrderProductDownload> getByOrderId(Long orderId) {
    	return orderProductDownloadRepository.findByOrderId(orderId);
    }


}
