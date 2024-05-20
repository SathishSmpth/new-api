package com.kamatchibotique.application.service.services.catalog.product.attribute;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductOptionSet;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ProductOptionSetService extends CommonService<Long, ProductOptionSet> {

	List<ProductOptionSet> listByStore(MerchantStore store, Language language)
			throws ServiceException;


	ProductOptionSet getById(MerchantStore store, Long optionId, Language lang);
	ProductOptionSet getCode(MerchantStore store, String code);
	List<ProductOptionSet> getByProductType (Long productTypeId, MerchantStore store, Language lang);
}
