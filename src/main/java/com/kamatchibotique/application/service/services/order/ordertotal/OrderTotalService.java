package com.kamatchibotique.application.service.services.order.ordertotal;

import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.OrderSummary;
import com.kamatchibotique.application.model.order.OrderTotalVariation;
import com.kamatchibotique.application.model.reference.language.Language;

public interface OrderTotalService {
	
	OrderTotalVariation findOrderTotalVariation(final OrderSummary summary, final Customer customer, final MerchantStore store, final Language language) throws Exception;

}
