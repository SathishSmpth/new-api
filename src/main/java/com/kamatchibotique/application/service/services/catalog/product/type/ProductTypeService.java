package com.kamatchibotique.application.service.services.catalog.product.type;

import java.util.List;

import org.springframework.data.domain.Page;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.type.ProductType;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ProductTypeService extends CommonService<Long, ProductType> {

	ProductType getProductType(String productTypeCode);
	Page<ProductType> getByMerchant(MerchantStore store, Language language, int page, int count) throws ServiceException;
    ProductType getByCode(String code, MerchantStore store, Language language) throws ServiceException;
    ProductType getById(Long id, MerchantStore store, Language language) throws ServiceException;
    ProductType getById(Long id, MerchantStore store) throws ServiceException;
    void update(String code, MerchantStore store, ProductType type) throws ServiceException;
    ProductType saveOrUpdate(ProductType productType) throws ServiceException;
    List<ProductType> listProductTypes(List<Long> ids, MerchantStore store, Language language) throws ServiceException;
}
