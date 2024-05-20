package com.kamatchibotique.application.service.services.catalog.product.manufacturer;

import java.util.List;
import org.springframework.data.domain.Page;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.category.Category;
import com.kamatchibotique.application.model.catalog.product.manufacturer.Manufacturer;
import com.kamatchibotique.application.model.catalog.product.manufacturer.ManufacturerDescription;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

public interface ManufacturerService extends CommonService<Long, Manufacturer> {

	List<Manufacturer> listByStore(MerchantStore store, Language language)
			throws ServiceException;

	List<Manufacturer> listByStore(MerchantStore store) throws ServiceException;
	
	Page<Manufacturer> listByStore(MerchantStore store, Language language, int page, int count) throws ServiceException;

	Page<Manufacturer> listByStore(MerchantStore store, Language language, String name, int page, int count) throws ServiceException;
	
	void saveOrUpdate(Manufacturer manufacturer) throws ServiceException;
	
	void addManufacturerDescription(Manufacturer manufacturer, ManufacturerDescription description) throws ServiceException;
	
	Long getCountManufAttachedProducts( Manufacturer manufacturer )  throws ServiceException;
	
	void delete(Manufacturer manufacturer) throws ServiceException;
	
	Manufacturer getByCode(MerchantStore store, String code);
	List<Manufacturer> listByProductsByCategoriesId(MerchantStore store,
			List<Long> ids, Language language) throws ServiceException;

	List<Manufacturer> listByProductsInCategory(MerchantStore store,
        Category category, Language language) throws ServiceException;
	
	Page<Manufacturer> listByStore(MerchantStore store, String name,
	      int page, int count) throws ServiceException;
	
	int count(MerchantStore store);

	
}
