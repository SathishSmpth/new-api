package com.kamatchibotique.application.order.total;

import java.math.BigDecimal;

import com.kamatchibotique.application.enums.order.OrderTotalType;
import com.kamatchibotique.application.service.services.catalog.pricing.PricingService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.price.FinalPrice;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.OrderSummary;
import com.kamatchibotique.application.model.order.OrderTotal;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;


/**
 * Add variation to the OrderTotal
 * This has to be defined in shopizer-core-ordertotal-processors
 * @author carlsamson
 *
 */
@Component
public class ManufacturerShippingCodeOrderTotalModuleImpl implements OrderTotalPostProcessorModule {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerShippingCodeOrderTotalModuleImpl.class);
	
	private String name;
	private String code;
	
	//private StatelessKnowledgeSession orderTotalMethodDecision;//injected from xml file
	
	//private KnowledgeBase kbase;//injected from xml file
	
	//@Autowired
	//KieContainer kieManufacturerBasedPricingContainer;
	

	PricingService pricingService;
	

	
	public PricingService getPricingService() {
		return pricingService;
	}

	public void setPricingService(PricingService pricingService) {
		this.pricingService = pricingService;
	}

	@Override
	public OrderTotal caculateProductPiceVariation(final OrderSummary summary, ShoppingCartItem shoppingCartItem, Product product, Customer customer, MerchantStore store)
			throws Exception {

		
	    Validate.notNull(product,"product must not be null");
		Validate.notNull(product.getManufacturer(),"product manufacturer must not be null");
		
		//requires shipping summary, otherwise return null
		if(summary.getShippingSummary()==null) {
			return null;
		}

		OrderTotalInputParameters inputParameters = new OrderTotalInputParameters();
		inputParameters.setItemManufacturerCode(product.getManufacturer().getCode());
		
		
		inputParameters.setShippingMethod(summary.getShippingSummary().getShippingOptionCode());
		
		LOGGER.debug("Setting input parameters " + inputParameters.toString());
		
/*        KieSession kieSession = kieManufacturerBasedPricingContainer.newKieSession();
        kieSession.insert(inputParameters);
        kieSession.fireAllRules();*/
		
		
		//orderTotalMethodDecision.execute(inputParameters);
		
		
		LOGGER.debug("Applied discount " + inputParameters.getDiscount());
		
		OrderTotal orderTotal = null;
		if(inputParameters.getDiscount() != null) {
				orderTotal = new OrderTotal();
				orderTotal.setOrderTotalCode(Constants.OT_DISCOUNT_TITLE);
				orderTotal.setOrderTotalType(OrderTotalType.SUBTOTAL);
				orderTotal.setTitle(Constants.OT_SUBTOTAL_MODULE_CODE);
				
				//calculate discount that will be added as a negative value
				FinalPrice productPrice = pricingService.calculateProductPrice(product);
				
				Double discount = inputParameters.getDiscount();
				BigDecimal reduction = productPrice.getFinalPrice().multiply(new BigDecimal(discount));
				reduction = reduction.multiply(new BigDecimal(shoppingCartItem.getQuantity()));
				
				orderTotal.setValue(reduction);
		}
			
		
		
		return orderTotal;


	}
	
/*	public KnowledgeBase getKbase() {
		return kbase;
	}


	public void setKbase(KnowledgeBase kbase) {
		this.kbase = kbase;
	}

	public StatelessKnowledgeSession getOrderTotalMethodDecision() {
		return orderTotalMethodDecision;
	}

	public void setOrderTotalMethodDecision(StatelessKnowledgeSession orderTotalMethodDecision) {
		this.orderTotalMethodDecision = orderTotalMethodDecision;
	}*/

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}



}
