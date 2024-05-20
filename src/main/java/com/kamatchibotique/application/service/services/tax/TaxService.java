package com.kamatchibotique.application.service.services.tax;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.OrderSummary;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.tax.TaxConfiguration;
import com.kamatchibotique.application.model.tax.TaxItem;


public interface TaxService   {

	TaxConfiguration getTaxConfiguration(MerchantStore store)
			throws ServiceException;

	void saveTaxConfiguration(TaxConfiguration shippingConfiguration,
			MerchantStore store) throws ServiceException;

	List<TaxItem> calculateTax(OrderSummary orderSummary, Customer customer,
			MerchantStore store, Language language) throws ServiceException;
}
