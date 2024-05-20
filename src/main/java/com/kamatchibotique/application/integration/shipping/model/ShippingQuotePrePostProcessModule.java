package com.kamatchibotique.application.integration.shipping.model;

import com.kamatchibotique.application.model.common.Delivery;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.shipping.PackageDetails;
import com.kamatchibotique.application.model.shipping.ShippingConfiguration;
import com.kamatchibotique.application.model.shipping.ShippingOrigin;
import com.kamatchibotique.application.model.shipping.ShippingQuote;
import com.kamatchibotique.application.model.system.IntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationModule;
import com.kamatchibotique.application.integration.IntegrationExceptionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

/**
 * Invoked before or after quote processing
 * @author carlsamson
 *
 */
public interface ShippingQuotePrePostProcessModule {
	
	
	public String getModuleCode();
	

	public void prePostProcessShippingQuotes(
			ShippingQuote quote, 
			List<PackageDetails> packages, 
			BigDecimal orderTotal, 
			Delivery delivery, 
			ShippingOrigin origin, 
			MerchantStore store, 
			IntegrationConfiguration globalShippingConfiguration, 
			IntegrationModule currentModule, 
			ShippingConfiguration shippingConfiguration, 
			List<IntegrationModule> allModules, 
			Locale locale) throws IntegrationExceptionService;

}
