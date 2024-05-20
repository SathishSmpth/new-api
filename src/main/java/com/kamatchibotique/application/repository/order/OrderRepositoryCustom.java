package com.kamatchibotique.application.repository.order;

import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.OrderCriteria;
import com.kamatchibotique.application.model.order.OrderList;




public interface OrderRepositoryCustom {

	OrderList listByStore(MerchantStore store, OrderCriteria criteria);
	OrderList listOrders(MerchantStore store, OrderCriteria criteria);
}
