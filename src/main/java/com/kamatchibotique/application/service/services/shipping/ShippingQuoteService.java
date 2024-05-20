package com.kamatchibotique.application.service.services.shipping;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.model.shipping.Quote;
import com.kamatchibotique.application.model.shipping.ShippingSummary;

import java.util.List;

public interface ShippingQuoteService extends CommonService<Long, Quote> {
	List<Quote> findByOrder(Order order) throws ServiceException;
	ShippingSummary getShippingSummary(Long quoteId, MerchantStore store) throws ServiceException;
}
