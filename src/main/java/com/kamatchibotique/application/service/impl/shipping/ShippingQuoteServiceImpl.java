package com.kamatchibotique.application.service.impl.shipping;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.Order;
import com.kamatchibotique.application.model.shipping.Quote;
import com.kamatchibotique.application.model.shipping.ShippingSummary;
import com.kamatchibotique.application.repository.shipping.ShippingQuoteRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.shipping.ShippingQuoteService;
import com.kamatchibotique.application.service.services.shipping.ShippingService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("shippingQuoteService")
public class ShippingQuoteServiceImpl extends CommonServiceImpl<Long, Quote> implements ShippingQuoteService {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingQuoteServiceImpl.class);
	
	private ShippingQuoteRepository shippingQuoteRepository;
	
	@Autowired
	private ShippingService shippingService;
	
	@Autowired
	public ShippingQuoteServiceImpl(ShippingQuoteRepository repository) {
		super(repository);
		this.shippingQuoteRepository = repository;
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Quote> findByOrder(Order order) throws ServiceException {
		Validate.notNull(order,"Order cannot be null");
		return this.shippingQuoteRepository.findByOrder(order.getId());
	}

	@Override
	public ShippingSummary getShippingSummary(Long quoteId, MerchantStore store) throws ServiceException {
		
		Validate.notNull(quoteId,"quoteId must not be null");
		
		Quote q = shippingQuoteRepository.getOne(quoteId);

		
		ShippingSummary quote = null;
		
		if(q != null) {
			
			quote = new ShippingSummary();
			quote.setDeliveryAddress(q.getDelivery());
			quote.setShipping(q.getPrice());
			quote.setShippingModule(q.getModule());
			quote.setShippingOption(q.getOptionName());
			quote.setShippingOptionCode(q.getOptionCode());
			quote.setHandling(q.getHandling());
			
			if(shippingService.hasTaxOnShipping(store)) {
				quote.setTaxOnShipping(true);
			}
			
			
			
		}
		
		
		return quote;
		
	}


}
