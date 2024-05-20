package com.kamatchibotique.application.service.services.catalog.product.variant;

import java.util.Optional;

import org.springframework.data.domain.Page;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariantGroup;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ProductVariantGroupService extends CommonService<Long, ProductVariantGroup> {

	
	Optional<ProductVariantGroup> getById(Long id, MerchantStore store);
	
	Optional<ProductVariantGroup> getByProductVariant(Long productVariantId, MerchantStore store, Language language);

	Page<ProductVariantGroup> getByProductId(MerchantStore store, Long productId, Language language, int page, int count);

	ProductVariantGroup saveOrUpdate(ProductVariantGroup entity) throws ServiceException;
	
	
}
