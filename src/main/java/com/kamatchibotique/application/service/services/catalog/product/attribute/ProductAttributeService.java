package com.kamatchibotique.application.service.services.catalog.product.attribute;

import java.util.List;

import com.kamatchibotique.application.service.services.common.CommonService;
import org.springframework.data.domain.Page;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductAttribute;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ProductAttributeService extends
		CommonService<Long, ProductAttribute> {

	ProductAttribute saveOrUpdate(ProductAttribute productAttribute)
			throws ServiceException;
	
	List<ProductAttribute> getByOptionId(MerchantStore store,
			Long id) throws ServiceException;

	List<ProductAttribute> getByOptionValueId(MerchantStore store,
			Long id) throws ServiceException;

	Page<ProductAttribute> getByProductId(MerchantStore store, Product product, Language language, int page, int count)
			throws ServiceException;
	
	Page<ProductAttribute> getByProductId(MerchantStore store, Product product, int page, int count)
			throws ServiceException;

	List<ProductAttribute> getByAttributeIds(MerchantStore store, Product product, List<Long> ids)
			throws ServiceException;
	
	List<ProductAttribute> getProductAttributesByCategoryLineage(MerchantStore store, String lineage, Language language) throws Exception;
}
