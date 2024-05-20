package com.kamatchibotique.application.integration.shipping.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.kamatchibotique.application.integration.shipping.model.ShippingQuoteModule;
import com.kamatchibotique.application.integration.shipping.model.ShippingQuotePrePostProcessModule;
import com.kamatchibotique.application.service.services.system.MerchantConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.lang3.Validate;

import com.kamatchibotique.application.utils.ProductPriceUtils;
import com.kamatchibotique.application.model.common.Delivery;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.shipping.PackageDetails;
import com.kamatchibotique.application.model.shipping.ShippingConfiguration;
import com.kamatchibotique.application.model.shipping.ShippingOption;
import com.kamatchibotique.application.model.shipping.ShippingOrigin;
import com.kamatchibotique.application.model.shipping.ShippingQuote;
import com.kamatchibotique.application.model.system.CustomIntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationModule;
import com.kamatchibotique.application.integration.IntegrationExceptionService;


public class StorePickupShippingQuote implements ShippingQuoteModule, ShippingQuotePrePostProcessModule {
	
	
	public final static String MODULE_CODE = "storePickUp";

	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
	@Autowired
	private ProductPriceUtils productPriceUtils;


	@Override
	public void validateModuleConfiguration(
			IntegrationConfiguration integrationConfiguration,
			MerchantStore store) throws IntegrationExceptionService {
		
		
		
		
		List<String> errorFields = null;
		
		//validate integrationKeys['account']
		Map<String,String> keys = integrationConfiguration.getIntegrationKeys();
		//if(keys==null || StringUtils.isBlank(keys.get("price"))) {
		if(keys==null) {
			errorFields = new ArrayList<String>();
			errorFields.add("price");
		} else {
			//validate it can be parsed to BigDecimal
			try {
				BigDecimal price = new BigDecimal(keys.get("price"));
			} catch(Exception e) {
				errorFields = new ArrayList<String>();
				errorFields.add("price");
			}
		}
		
		//if(keys==null || StringUtils.isBlank(keys.get("note"))) {
		if(keys==null) {
			errorFields = new ArrayList<String>();
			errorFields.add("note");
		}


		
		if(errorFields!=null) {
			IntegrationExceptionService ex = new IntegrationExceptionService(IntegrationExceptionService.ERROR_VALIDATION_SAVE);
			ex.setErrorFields(errorFields);
			throw ex;
			
		}

	}

	@Override
	public List<ShippingOption> getShippingQuotes(
			ShippingQuote shippingQuote,
			List<PackageDetails> packages, BigDecimal orderTotal,
			Delivery delivery, ShippingOrigin origin, MerchantStore store,
			IntegrationConfiguration configuration, IntegrationModule module,
			ShippingConfiguration shippingConfiguration, Locale locale)
			throws IntegrationExceptionService {

		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public CustomIntegrationConfiguration getCustomModuleConfiguration(
			MerchantStore store) throws IntegrationExceptionService {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prePostProcessShippingQuotes(ShippingQuote quote,
			List<PackageDetails> packages, BigDecimal orderTotal,
			Delivery delivery, ShippingOrigin origin, MerchantStore store,
			IntegrationConfiguration globalShippingConfiguration,
			IntegrationModule currentModule,
			ShippingConfiguration shippingConfiguration,
			List<IntegrationModule> allModules, Locale locale)
			throws IntegrationExceptionService {
		
		Validate.notNull(globalShippingConfiguration, "IntegrationConfiguration must not be null for StorePickUp");
		
		
		try {
			
			if(!globalShippingConfiguration.isActive())
				return;

			String region = null;
			
			String price = globalShippingConfiguration.getIntegrationKeys().get("price");
	
	
			if(delivery.getZone()!=null) {
				region = delivery.getZone().getCode();
			} else {
				region = delivery.getState();
			}
			
			ShippingOption shippingOption = new ShippingOption();
			shippingOption.setShippingModuleCode(MODULE_CODE);
			shippingOption.setOptionCode(MODULE_CODE);
			shippingOption.setOptionId(new StringBuilder().append(MODULE_CODE).append("_").append(region).toString());
			
			shippingOption.setOptionPrice(productPriceUtils.getAmount(price));
	
			shippingOption.setOptionPriceText(productPriceUtils.getStoreFormatedAmountWithCurrency(store, productPriceUtils.getAmount(price)));
	
			List<ShippingOption> options = quote.getShippingOptions();
			
			if(options == null) {
				options = new ArrayList<ShippingOption>();
				quote.setShippingOptions(options);
			}

			options.add(shippingOption);
			
			if(quote.getSelectedShippingOption()==null) {
				quote.setSelectedShippingOption(shippingOption);
			}

		
		} catch (Exception e) {
			throw new IntegrationExceptionService(e);
		}
	
		
		
	}

	@Override
	public String getModuleCode() {
		// TODO Auto-generated method stub
		return MODULE_CODE;
	}



}
