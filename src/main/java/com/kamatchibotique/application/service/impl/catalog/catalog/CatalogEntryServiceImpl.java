package com.kamatchibotique.application.service.impl.catalog.catalog;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.catalog.catalog.CatalogEntryRepository;
import com.kamatchibotique.application.repository.catalog.catalog.PageableCatalogEntryRepository;
import com.kamatchibotique.application.service.services.catalog.catalog.CatalogEntryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.kamatchibotique.application.model.catalog.catalog.Catalog;
import com.kamatchibotique.application.model.catalog.catalog.CatalogCategoryEntry;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

@AllArgsConstructor
@Service("catalogEntryService")
public class CatalogEntryServiceImpl implements CatalogEntryService {
	
	@Autowired
	private PageableCatalogEntryRepository pageableCatalogEntryRepository;

	@Autowired
	private CatalogEntryRepository catalogEntryRepository;

	@Override
	public void add(CatalogCategoryEntry entry, Catalog catalog) {
		entry.setCatalog(catalog);
		catalogEntryRepository.save(entry);
	}


	@Override
	public Page<CatalogCategoryEntry> list(Catalog catalog, MerchantStore store, Language language, String name, int page,
			int count) {
		Pageable pageRequest = PageRequest.of(page, count);
		return pageableCatalogEntryRepository.listByCatalog(catalog.getId(), store.getId(), language.getId(), name, pageRequest);

	}

	@Override
	public void remove(CatalogCategoryEntry catalogEntry) throws ServiceException {
		catalogEntryRepository.delete(catalogEntry);
		
	}


}
