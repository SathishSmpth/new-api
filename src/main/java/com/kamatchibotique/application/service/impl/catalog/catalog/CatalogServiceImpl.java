package com.kamatchibotique.application.service.impl.catalog.catalog;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.catalog.Catalog;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.repository.catalog.catalog.CatalogRepository;
import com.kamatchibotique.application.repository.catalog.catalog.PageableCatalogRepository;
import com.kamatchibotique.application.service.services.catalog.catalog.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Objects;
import java.util.Optional;

public class CatalogServiceImpl implements CatalogService {
    private CatalogRepository catalogRepository;

    @Autowired
    private PageableCatalogRepository pageableCatalogRepository;

    @Override
    public Catalog saveOrUpdate(Catalog catalog, MerchantStore store) {
        catalogRepository.save(catalog);
        return catalog;
    }

    @Override
    public Page<Catalog> getCatalogs(MerchantStore store, Language language, String name, int page, int count) {
        Pageable pageRequest = PageRequest.of(page, count);
        return pageableCatalogRepository.listByStore(store.getId(), name, pageRequest);
    }

    @Override
    public void delete(Catalog catalog) throws ServiceException {
       Objects.requireNonNull(catalog, "Catalog must not be null");
        catalogRepository.delete(catalog);
    }

    @Override
    public Optional<Catalog> getById(Long catalogId, MerchantStore store) {
        return catalogRepository.findById(catalogId, store.getId());
    }

    @Override
    public Optional<Catalog> getByCode(String code, MerchantStore store) {
        return catalogRepository.findByCode(code, store.getId());
    }

    @Override
    public boolean existByCode(String code, MerchantStore store) {
        return catalogRepository.existsByCode(code, store.getId());
    }
}
