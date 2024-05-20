package com.kamatchibotique.application.service.services.order.orderproduct;

import java.util.List;

import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.order.orderproduct.OrderProductDownload;


public interface OrderProductDownloadService extends CommonService<Long, OrderProductDownload> {

	/**
	 * List {@link OrderProductDownload} by order id
	 * @param orderId
	 * @return
	 */
	List<OrderProductDownload> getByOrderId(Long orderId);

}
