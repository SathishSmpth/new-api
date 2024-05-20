package com.kamatchibotique.application.service.services.catalog.product.attribute;

import java.util.List;

import com.kamatchibotique.application.service.services.common.CommonService;
import org.springframework.data.domain.Page;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductOptionValue;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ProductOptionValueService extends CommonService<Long, ProductOptionValue> {

	void saveOrUpdate(ProductOptionValue entity) throws ServiceException;

	List<ProductOptionValue> getByName(MerchantStore store, String name,
			Language language) throws ServiceException;


	List<ProductOptionValue> listByStore(MerchantStore store, Language language)
			throws ServiceException;

	List<ProductOptionValue> listByStoreNoReadOnly(MerchantStore store,
			Language language) throws ServiceException;

	ProductOptionValue getByCode(MerchantStore store, String optionValueCode);
	
	ProductOptionValue getById(MerchantStore store, Long optionValueId);
	
	Page<ProductOptionValue> getByMerchant(MerchantStore store, Language language, String name, int page, int count);
	

}
