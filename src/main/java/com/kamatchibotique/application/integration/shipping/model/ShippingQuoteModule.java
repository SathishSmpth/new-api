package com.kamatchibotique.application.integration.shipping.model;

import com.kamatchibotique.application.integration.IntegrationExceptionService;
import com.kamatchibotique.application.model.common.Delivery;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.shipping.*;
import com.kamatchibotique.application.model.system.CustomIntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationModule;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public interface ShippingQuoteModule {
	
	public void validateModuleConfiguration(IntegrationConfiguration integrationConfiguration, MerchantStore store) throws IntegrationExceptionService;
	public CustomIntegrationConfiguration getCustomModuleConfiguration(MerchantStore store) throws IntegrationExceptionService;
	
	public List<ShippingOption> getShippingQuotes(ShippingQuote quote, List<PackageDetails> packages, BigDecimal orderTotal, Delivery delivery, ShippingOrigin origin, MerchantStore store, IntegrationConfiguration configuration, IntegrationModule module, ShippingConfiguration shippingConfiguration, Locale locale) throws IntegrationExceptionService;

}
