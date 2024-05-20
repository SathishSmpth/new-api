package com.kamatchibotique.application.service.services.catalog.product.attribute;

import java.util.List;

import com.kamatchibotique.application.service.services.common.CommonService;
import org.springframework.data.domain.Page;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductOption;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ProductOptionService extends CommonService<Long, ProductOption> {

	List<ProductOption> listByStore(MerchantStore store, Language language)
			throws ServiceException;


	List<ProductOption> getByName(MerchantStore store, String name,
			Language language) throws ServiceException;

	void saveOrUpdate(ProductOption entity) throws ServiceException;


	List<ProductOption> listReadOnly(MerchantStore store, Language language)
			throws ServiceException;


	ProductOption getByCode(MerchantStore store, String optionCode);
	
	ProductOption getById(MerchantStore store, Long optionId);
	
	Page<ProductOption> getByMerchant(MerchantStore store, Language language, String name, int page, int count);
}
