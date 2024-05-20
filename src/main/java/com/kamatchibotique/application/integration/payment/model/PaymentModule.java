package com.kamatchibotique.application.integration.payment.model;

import com.kamatchibotique.application.integration.IntegrationExceptionService;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.model.payments.Payment;
import com.kamatchibotique.application.model.payments.Transaction;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;
import com.kamatchibotique.application.model.system.IntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationModule;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentModule {
	
	public void validateModuleConfiguration(IntegrationConfiguration integrationConfiguration, MerchantStore store) throws IntegrationExceptionService;

	public Transaction initTransaction(
			MerchantStore store, Customer customer, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationExceptionService;
	
	public Transaction authorize(
			MerchantStore store, Customer customer, List<ShoppingCartItem> items, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationExceptionService;

	
	public Transaction capture(
			MerchantStore store, Customer customer, Order order, Transaction capturableTransaction, IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationExceptionService;
	
	public Transaction authorizeAndCapture(
			MerchantStore store, Customer customer, List<ShoppingCartItem> items, BigDecimal amount, Payment payment, IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationExceptionService;
	
	public Transaction refund(
			boolean partial, MerchantStore store, Transaction transaction, Order order, BigDecimal amount, IntegrationConfiguration configuration, IntegrationModule module)
			throws IntegrationExceptionService;

}
