package com.kamatchibotique.application.service.services.payments;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.kamatchibotique.application.enums.payment.CreditCardType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.integration.payment.model.PaymentModule;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.model.payments.Payment;
import com.kamatchibotique.application.model.payments.PaymentMethod;
import com.kamatchibotique.application.model.payments.Transaction;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;
import com.kamatchibotique.application.model.system.IntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationModule;

public interface PaymentService {

	List<IntegrationModule> getPaymentMethods(MerchantStore store)
			throws ServiceException;

	Map<String, IntegrationConfiguration> getPaymentModulesConfigured(
			MerchantStore store) throws ServiceException;
	
	Transaction processPayment(Customer customer, MerchantStore store, Payment payment, List<ShoppingCartItem> items, Order order) throws ServiceException;
	Transaction processRefund(Order order, Customer customer, MerchantStore store, BigDecimal amount) throws ServiceException;

	IntegrationModule getPaymentMethodByType(MerchantStore store, String type)
			throws ServiceException;

	IntegrationModule getPaymentMethodByCode(MerchantStore store, String name)
			throws ServiceException;

	void savePaymentModuleConfiguration(IntegrationConfiguration configuration,
			MerchantStore store) throws ServiceException;

	void validateCreditCard(String number, CreditCardType creditCard, String month, String date)
			throws ServiceException;

	IntegrationConfiguration getPaymentConfiguration(String moduleCode,
			MerchantStore store) throws ServiceException;

	void removePaymentModuleConfiguration(String moduleCode, MerchantStore store)
			throws ServiceException;

	Transaction processCapturePayment(Order order, Customer customer,
			MerchantStore store)
			throws ServiceException;
	Transaction initTransaction(Order order, Customer customer, Payment payment, MerchantStore store) throws ServiceException;

	Transaction initTransaction(Customer customer, Payment payment, MerchantStore store) throws ServiceException;

	List<PaymentMethod> getAcceptedPaymentMethods(MerchantStore store)
			throws ServiceException;

	PaymentModule getPaymentModule(String paymentModuleCode)
			throws ServiceException;
}