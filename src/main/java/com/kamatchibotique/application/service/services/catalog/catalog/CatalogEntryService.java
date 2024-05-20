package com.kamatchibotique.application.service.services.catalog.catalog;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.catalog.Catalog;
import com.kamatchibotique.application.model.catalog.catalog.CatalogCategoryEntry;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import org.springframework.data.domain.Page;

public interface CatalogEntryService {
    void add(CatalogCategoryEntry entry, Catalog catalog);

    void remove(CatalogCategoryEntry catalogEntry) throws ServiceException;

    Page<CatalogCategoryEntry> list(Catalog catalog, MerchantStore store, Language language, String name, int page, int count);
}
