package com.kamatchibotique.application.service.services.shoppingcart;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.OrderTotalSummary;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCart;

public interface ShoppingCartCalculationService {

	OrderTotalSummary calculate(final ShoppingCart cartModel, final Customer customer, final MerchantStore store,
			final Language language) throws ServiceException;

	OrderTotalSummary calculate(final ShoppingCart cartModel, final MerchantStore store, final Language language)
			throws ServiceException;
}
