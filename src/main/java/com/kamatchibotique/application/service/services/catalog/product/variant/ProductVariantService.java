package com.kamatchibotique.application.service.services.catalog.product.variant;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariant;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ProductVariantService extends CommonService<Long, ProductVariant> {
	
	Optional<ProductVariant> getById(Long id, Long productId, MerchantStore store);
	
	List<ProductVariant> getByIds(List<Long> ids, MerchantStore store);
	
	Optional<ProductVariant> getById(Long id, MerchantStore store);
	
	Optional<ProductVariant> getBySku(String sku, Long productId, MerchantStore store, Language language);
	
	List<ProductVariant> getByProductId(MerchantStore store, Product product, Language language);
	
	
	Page<ProductVariant> getByProductId(MerchantStore store, Product product, Language language, int page, int count);
	
	
	boolean exist(String sku, Long productId);
	
	ProductVariant saveProductVariant(ProductVariant variant) throws ServiceException;
	


}
