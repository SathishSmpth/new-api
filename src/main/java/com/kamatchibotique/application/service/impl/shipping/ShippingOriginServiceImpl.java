package com.kamatchibotique.application.service.impl.shipping;

import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.shipping.ShippingOrigin;
import com.kamatchibotique.application.repository.shipping.ShippingOriginRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.shipping.ShippingOriginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("shippingOriginService")
public class ShippingOriginServiceImpl extends CommonServiceImpl<Long, ShippingOrigin> implements ShippingOriginService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShippingOriginServiceImpl.class);
	
	private ShippingOriginRepository shippingOriginRepository;

	

	@Autowired
	public ShippingOriginServiceImpl(ShippingOriginRepository shippingOriginRepository) {
		super(shippingOriginRepository);
		this.shippingOriginRepository = shippingOriginRepository;
	}


	@Override
	public ShippingOrigin getByStore(MerchantStore store) {
		// TODO Auto-generated method stub
		return shippingOriginRepository.findByStore(store.getId());
	}
	

}
