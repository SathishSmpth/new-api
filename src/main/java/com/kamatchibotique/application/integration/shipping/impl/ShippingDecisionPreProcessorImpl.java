package com.kamatchibotique.application.integration.shipping.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.kamatchibotique.application.integration.shipping.model.ShippingQuotePrePostProcessModule;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.kamatchibotique.application.model.shipping.ShippingOrigin;
import com.kamatchibotique.application.model.shipping.ShippingQuote;
import com.kamatchibotique.application.model.system.IntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationModule;
import com.kamatchibotique.application.modules.constants.Constants;
import com.kamatchibotique.application.integration.IntegrationExceptionService;

@AllArgsConstructor
@NoArgsConstructor
public class ShippingDecisionPreProcessorImpl implements ShippingQuotePrePostProcessModule {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingDecisionPreProcessorImpl.class);
	
	private final static String MODULE_CODE = "shippingDecisionModule";
	
	@Autowired
	private DroolsBeanFactory droolsBeanFactory;
	
	//private StatelessKnowledgeSession shippingMethodDecision;
	
	//private KnowledgeBase kbase;
	
	//@Autowired
	//KieContainer kieShippingDecisionContainer;
	
	@Override
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
			Locale locale)
			throws IntegrationExceptionService {
		
		
		Validate.notNull(delivery, "Delivery cannot be null");
		Validate.notNull(currentModule, "IntegrationModule cannot be null");
		Validate.notNull(delivery.getCountry(), "Delivery.country cannot be null");
		Validate.notNull(allModules, "List<IntegrationModule> cannot be null");
		Validate.notNull(packages, "packages cannot be null");
		Validate.notEmpty(packages, "packages cannot be empty");
		
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
		//calculate weight, volume and largest size
		for(PackageDetails pack : packages) {
			weight = weight + pack.getShippingWeight();
			Double tmpVolume = pack.getShippingHeight() * pack.getShippingLength() * pack.getShippingWidth();
			if(volume == null || tmpVolume > volume) { //take the largest volume
				volume = tmpVolume;
			} 
			//largest size
			List<Double> sizeList = new ArrayList<Double>();
			sizeList.add(pack.getShippingHeight());
			sizeList.add(pack.getShippingLength());
			sizeList.add(pack.getShippingWidth());
			Double maxSize = Collections.max(sizeList);
			if(size==null || maxSize > size) {
				size = maxSize;
			}
		}
		
		//Build a ShippingInputParameters
		ShippingInputParameters inputParameters = new ShippingInputParameters();
		
		inputParameters.setWeight((long)weight.doubleValue());
		inputParameters.setCountry(delivery.getCountry().getIsoCode());
		if(delivery.getZone()!=null && delivery.getZone().getCode()!=null) {
			inputParameters.setProvince(delivery.getZone().getCode());
		} else {
			inputParameters.setProvince(delivery.getState());
		}
		//inputParameters.setModuleName(currentModule.getCode());
		
		
		if(size!=null) {
			inputParameters.setSize((long)size.doubleValue());
		}
		
		if(distance!=null) {
			double ddistance = distance;
			long ldistance = (long)ddistance;
			inputParameters.setDistance(ldistance);
		}
		
		if(volume!=null) {
			inputParameters.setVolume((long)volume.doubleValue());
		}
		
		LOGGER.debug("Setting input parameters " + inputParameters.toString());
		System.out.println(inputParameters.toString());
		
		
		/**
		 * New code
		 */
		
		KieSession kieSession=droolsBeanFactory.getKieSession(ResourceFactory.newClassPathResource("com/salesmanager/drools/rules/ShippingDecision.drl"));
		
		DecisionResponse resp = new DecisionResponse();
		
        kieSession.insert(inputParameters);
        kieSession.setGlobal("decision",resp);
        kieSession.fireAllRules();
        //System.out.println(resp.getModuleName());
        inputParameters.setModuleName(resp.getModuleName());

		LOGGER.debug("Using shipping nodule " + inputParameters.getModuleName());
		
		if(!StringUtils.isBlank(inputParameters.getModuleName())) {
			for(IntegrationModule toBeUsed : allModules) {
				if(toBeUsed.getCode().equals(inputParameters.getModuleName())) {
					quote.setCurrentShippingModule(toBeUsed);
					break;
				}
			}
		}
		
	}


	@Override
	public String getModuleCode() {
		return MODULE_CODE;
	}
	







}
