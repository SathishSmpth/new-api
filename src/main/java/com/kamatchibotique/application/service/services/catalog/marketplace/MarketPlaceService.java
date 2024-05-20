package com.kamatchibotique.application.service.services.catalog.marketplace;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.marketplace.MarketPlace;
import com.kamatchibotique.application.model.merchant.MerchantStore;

public interface MarketPlaceService extends CommonService<Long, MarketPlace> {
	MarketPlace create(MerchantStore store, String code) throws ServiceException;

	MarketPlace getByCode(MerchantStore store, String code) throws ServiceException;
	
	void delete(MarketPlace marketPlace) throws ServiceException;
}
