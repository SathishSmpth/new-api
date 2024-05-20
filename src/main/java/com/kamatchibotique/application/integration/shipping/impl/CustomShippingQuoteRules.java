package com.kamatchibotique.application.integration.shipping.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.kamatchibotique.application.integration.shipping.model.ShippingQuoteModule;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kamatchibotique.application.configuration.DroolsBeanFactory;
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
import com.kamatchibotique.application.modules.constants.Constants;
import com.kamatchibotique.application.integration.IntegrationExceptionService;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomShippingQuoteRules implements ShippingQuoteModule {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomShippingQuoteRules.class);
	
	@Autowired
	private DroolsBeanFactory droolsBeanFactory;

	public final static String MODULE_CODE = "customQuotesRules";

	@Override
	public void validateModuleConfiguration(
			IntegrationConfiguration integrationConfiguration,
			MerchantStore store) throws IntegrationExceptionService {
		// Not used

	}

	@Override
	public CustomIntegrationConfiguration getCustomModuleConfiguration(
			MerchantStore store) throws IntegrationExceptionService {
		// Not used
		return null;
	}

	@Override
	public List<ShippingOption> getShippingQuotes(ShippingQuote quote,
			List<PackageDetails> packages, BigDecimal orderTotal,
			Delivery delivery, ShippingOrigin origin, MerchantStore store,
			IntegrationConfiguration configuration, IntegrationModule module,
			ShippingConfiguration shippingConfiguration, Locale locale)
			throws IntegrationExceptionService {

		
		
		Validate.notNull(delivery, "Delivery cannot be null");
		Validate.notNull(delivery.getCountry(), "Delivery.country cannot be null");
		Validate.notNull(packages, "packages cannot be null");
		Validate.notEmpty(packages, "packages cannot be empty");
		
		//requires the postal code
		if(StringUtils.isBlank(delivery.getPostalCode())) {
			return null;
		}

		Double distance = null;
		
		if(quote!=null) {
			//look if distance has been calculated
			if(quote.getQuoteInformations()!=null) {
				if(quote.getQuoteInformations().containsKey(Constants.DISTANCE_KEY)) {
					distance = (Double)quote.getQuoteInformations().get(Constants.DISTANCE_KEY);
				}
			}
		}
		
		//calculate volume (L x W x H)
		Double volume = null;
		Double weight = 0D;
		Double size = null;
		//calculate weight
		for(PackageDetails pack : packages) {
			weight = weight + pack.getShippingWeight();
			Double tmpVolume = pack.getShippingHeight() * pack.getShippingLength() * pack.getShippingWidth();
			if(volume == null || tmpVolume > volume) { //take the largest volume
				volume = tmpVolume;
			} 
			//largest size
			List<Double> sizeList = new ArrayList<Double>();
			sizeList.add(pack.getShippingHeight());
			sizeList.add(pack.getShippingWidth());
			sizeList.add(pack.getShippingLength());
			Double maxSize = Collections.max(sizeList);
			if(size==null || maxSize > size) {
				size = maxSize;
			}
		}
		
		//Build a ShippingInputParameters
		ShippingInputParameters inputParameters = new ShippingInputParameters();
		
		inputParameters.setWeight((long)weight.doubleValue());
		inputParameters.setCountry(delivery.getCountry().getIsoCode());
		inputParameters.setProvince("*");
		inputParameters.setModuleName(module.getCode());
		
		if(delivery.getZone() != null && delivery.getZone().getCode()!=null) {
			inputParameters.setProvince(delivery.getZone().getCode());
		}
		
		if(distance!=null) {
			double ddistance = distance;
			long ldistance = (long)ddistance;
			inputParameters.setDistance(ldistance);
		}
		
		if(volume!=null) {
			inputParameters.setVolume((long)volume.doubleValue());
		}
		
		List<ShippingOption> options = quote.getShippingOptions();
		
		if(options == null) {
			options = new ArrayList<ShippingOption>();
			quote.setShippingOptions(options);
		}
		
		
		
		LOGGER.debug("Setting input parameters " + inputParameters.toString());
		
		
		KieSession kieSession=droolsBeanFactory.getKieSession(ResourceFactory.newClassPathResource("com/salesmanager/drools/rules/PriceByDistance.drl"));
		
		DecisionResponse resp = new DecisionResponse();
		
        kieSession.insert(inputParameters);
        kieSession.setGlobal("decision",resp);
        kieSession.fireAllRules();
        //System.out.println(resp.getCustomPrice());

		if(resp.getCustomPrice() != null) {

			ShippingOption shippingOption = new ShippingOption();
			
			
			shippingOption.setOptionPrice(new BigDecimal(resp.getCustomPrice()));
			shippingOption.setShippingModuleCode(MODULE_CODE);
			shippingOption.setOptionCode(MODULE_CODE);
			shippingOption.setOptionId(MODULE_CODE);

			options.add(shippingOption);
		}

		
		return options;
		
		
	}

/*	public StatelessKnowledgeSession getShippingPriceRule() {
		return shippingPriceRule;
	}

	public void setShippingPriceRule(StatelessKnowledgeSession shippingPriceRule) {
		this.shippingPriceRule = shippingPriceRule;
	}

	public KnowledgeBase getKbase() {
		return kbase;
	}

	public void setKbase(KnowledgeBase kbase) {
		this.kbase = kbase;
	}*/

}
