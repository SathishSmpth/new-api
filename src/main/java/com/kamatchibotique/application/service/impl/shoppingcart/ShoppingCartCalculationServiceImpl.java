/**
 *
 */
package com.kamatchibotique.application.service.impl.shoppingcart;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.OrderTotalSummary;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCart;
import com.kamatchibotique.application.service.services.order.OrderService;
import com.kamatchibotique.application.service.services.shoppingcart.ShoppingCartCalculationService;
import com.kamatchibotique.application.service.services.shoppingcart.ShoppingCartService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("shoppingCartCalculationService")
public class ShoppingCartCalculationServiceImpl implements ShoppingCartCalculationService {

	protected final Logger LOG = LoggerFactory.getLogger(getClass());

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private OrderService orderService;

	@Override
	public OrderTotalSummary calculate(final ShoppingCart cartModel, final Customer customer, final MerchantStore store,
			final Language language) throws ServiceException {

		Validate.notNull(cartModel, "cart cannot be null");
		Validate.notNull(cartModel.getLineItems(), "Cart should have line items.");
		Validate.notNull(store, "MerchantStore cannot be null");
		Validate.notNull(customer, "Customer cannot be null");
		OrderTotalSummary orderTotalSummary = orderService.calculateShoppingCartTotal(cartModel, customer, store,
				language);
		updateCartModel(cartModel);
		return orderTotalSummary;

	}

	@Override
	public OrderTotalSummary calculate(final ShoppingCart cartModel, final MerchantStore store, final Language language)
			throws ServiceException {

		Validate.notNull(cartModel, "cart cannot be null");
		Validate.notNull(cartModel.getLineItems(), "Cart should have line items.");
		Validate.notNull(store, "MerchantStore cannot be null");
		OrderTotalSummary orderTotalSummary = orderService.calculateShoppingCartTotal(cartModel, store, language);
		updateCartModel(cartModel);
		return orderTotalSummary;

	}

	public ShoppingCartService getShoppingCartService() {
		return shoppingCartService;
	}

	private void updateCartModel(final ShoppingCart cartModel) throws ServiceException {
		shoppingCartService.saveOrUpdate(cartModel);
	}

}
