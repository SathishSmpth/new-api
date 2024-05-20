package com.kamatchibotique.application.order;

import java.io.ByteArrayOutputStream;

import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.model.reference.language.Language;


public interface InvoiceModule {
	
	ByteArrayOutputStream createInvoice(MerchantStore store, Order order, Language language) throws Exception;

}
