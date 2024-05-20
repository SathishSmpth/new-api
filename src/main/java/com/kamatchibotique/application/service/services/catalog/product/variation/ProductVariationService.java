package com.kamatchibotique.application.service.services.catalog.product.variation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.variation.ProductVariation;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ProductVariationService extends CommonService<Long, ProductVariation> {
	void saveOrUpdate(ProductVariation entity) throws ServiceException;

	Optional<ProductVariation> getById(MerchantStore store, Long id, Language lang);
	
	Optional<ProductVariation> getById(MerchantStore store, Long id);
	
	Optional<ProductVariation> getByCode(MerchantStore store, String code);
	
	Page<ProductVariation> getByMerchant(MerchantStore store, Language language, String code, int page, int count);
	
	List<ProductVariation> getByIds(List<Long> ids, MerchantStore store);
}
