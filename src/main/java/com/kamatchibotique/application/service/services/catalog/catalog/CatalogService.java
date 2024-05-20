package com.kamatchibotique.application.service.services.catalog.catalog;

import com.kamatchibotique.application.exception.ServiceException;
import org.springframework.data.domain.Page;

import com.kamatchibotique.application.model.catalog.catalog.Catalog;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

import java.util.Optional;

public interface CatalogService {

	Catalog saveOrUpdate(Catalog catalog, MerchantStore store);

	Optional<Catalog> getById(Long catalogId, MerchantStore store);

	Optional<Catalog> getByCode(String code, MerchantStore store);

	Page<Catalog> getCatalogs(MerchantStore store, Language language, String name, int page, int count);

	void delete(Catalog catalog) throws ServiceException;
	
	boolean existByCode(String code, MerchantStore store);

}
