package com.kamatchibotique.application.integration.shipping.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.kamatchibotique.application.enums.shipping.ShippingBasisType;
import com.kamatchibotique.application.integration.IntegrationExceptionService;
import com.kamatchibotique.application.integration.shipping.model.CustomShippingQuoteWeightItem;
import com.kamatchibotique.application.integration.shipping.model.CustomShippingQuotesConfiguration;
import com.kamatchibotique.application.integration.shipping.model.CustomShippingQuotesRegion;
import com.kamatchibotique.application.integration.shipping.model.ShippingQuoteModule;
import com.kamatchibotique.application.service.services.system.MerchantConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamatchibotique.application.exception.ServiceException;
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
import com.kamatchibotique.application.model.system.MerchantConfiguration;


public class CustomWeightBasedShippingQuote implements ShippingQuoteModule {
	
	public final static String MODULE_CODE = "weightBased";
	private final static String CUSTOM_WEIGHT = "CUSTOM_WEIGHT";
	
	@Autowired
	private MerchantConfigurationService merchantConfigurationService;
	
	@Autowired
	private ProductPriceUtils productPriceUtils;


	@Override
	public void validateModuleConfiguration(
			IntegrationConfiguration integrationConfiguration,
			MerchantStore store) throws IntegrationExceptionService {
		
		
		//not used, it has its own controller with complex validators

	}
	

	@Override
	public CustomIntegrationConfiguration getCustomModuleConfiguration(
			MerchantStore store) throws IntegrationExceptionService {

		try {

			MerchantConfiguration configuration = merchantConfigurationService.getMerchantConfiguration(MODULE_CODE, store);
	
			if(configuration!=null) {
				String value = configuration.getValue();
				ObjectMapper mapper = new ObjectMapper();
				try {
					return mapper.readValue(value, CustomShippingQuotesConfiguration.class);
				} catch(Exception e) {
					throw new ServiceException("Cannot parse json string " + value);
				}
	
			} else {
				CustomShippingQuotesConfiguration custom = new CustomShippingQuotesConfiguration();
				custom.setModuleCode(MODULE_CODE);
				return custom;
			}
		
		} catch (Exception e) {
			throw new IntegrationExceptionService(e);
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

		if(StringUtils.isBlank(delivery.getPostalCode())) {
			return null;
		}
		
		//get configuration
		CustomShippingQuotesConfiguration customConfiguration = (CustomShippingQuotesConfiguration)this.getCustomModuleConfiguration(store);
		
		
		List<CustomShippingQuotesRegion> regions = customConfiguration.getRegions();
		
		ShippingBasisType shippingType =  shippingConfiguration.getShippingBasisType();
		ShippingOption shippingOption = null;
		try {
			

			for(CustomShippingQuotesRegion region : customConfiguration.getRegions()) {
	
				for(String countryCode : region.getCountries()) {
					if(countryCode.equals(delivery.getCountry().getIsoCode())) {
						
						
						//determine shipping weight
						double weight = 0;
						for(PackageDetails packageDetail : packages) {
							weight = weight + packageDetail.getShippingWeight();
						}
						
						//see the price associated with the width
						List<CustomShippingQuoteWeightItem> quoteItems = region.getQuoteItems();
						for(CustomShippingQuoteWeightItem quoteItem : quoteItems) {
							if(weight<= quoteItem.getMaximumWeight()) {
								shippingOption = new ShippingOption();
								shippingOption.setOptionCode(new StringBuilder().append(CUSTOM_WEIGHT).toString());
								shippingOption.setOptionId(new StringBuilder().append(CUSTOM_WEIGHT).append("_").append(region.getCustomRegionName()).toString());
								shippingOption.setOptionPrice(quoteItem.getPrice());
								shippingOption.setOptionPriceText(productPriceUtils.getStoreFormatedAmountWithCurrency(store, quoteItem.getPrice()));
								break;
							}
						}
						
					}
					
					
				}
				
			}
			
			if(shippingOption!=null) {
				List<ShippingOption> options = new ArrayList<ShippingOption>();
				options.add(shippingOption);
				return options;
			}
			
			return null;
		
		} catch (Exception e) {
			throw new IntegrationExceptionService(e);
		}

	}



}
