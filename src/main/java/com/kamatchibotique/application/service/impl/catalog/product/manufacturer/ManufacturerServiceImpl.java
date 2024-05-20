package com.kamatchibotique.application.service.impl.catalog.product.manufacturer;


import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.category.Category;
import com.kamatchibotique.application.model.catalog.product.manufacturer.Manufacturer;
import com.kamatchibotique.application.model.catalog.product.manufacturer.ManufacturerDescription;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.repository.catalog.product.manufacturer.ManufacturerRepository;
import com.kamatchibotique.application.repository.catalog.product.manufacturer.PageableManufacturerRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.catalog.product.manufacturer.ManufacturerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;


@Service("manufacturerService")
public class ManufacturerServiceImpl extends CommonServiceImpl<Long, Manufacturer>
        implements ManufacturerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManufacturerServiceImpl.class);

    @Autowired
    private PageableManufacturerRepository pageableManufacturerRepository;

    private ManufacturerRepository manufacturerRepository;

    @Autowired
    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository) {
        super(manufacturerRepository);
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public void delete(Manufacturer manufacturer) throws ServiceException {
        manufacturer = this.getById(manufacturer.getId());
        super.delete(manufacturer);
    }

    @Override
    public Long getCountManufAttachedProducts(Manufacturer manufacturer) throws ServiceException {
        return manufacturerRepository.countByProduct(manufacturer.getId());
        // .getCountManufAttachedProducts( manufacturer );
    }


    @Override
    public List<Manufacturer> listByStore(MerchantStore store, Language language)
            throws ServiceException {
        return manufacturerRepository.findByStoreAndLanguage(store.getId(), language.getId());
    }

    @Override
    public List<Manufacturer> listByStore(MerchantStore store) throws ServiceException {
        return manufacturerRepository.findByStore(store.getId());
    }

    @Override
    public List<Manufacturer> listByProductsByCategoriesId(MerchantStore store, List<Long> ids,
                                                           Language language) throws ServiceException {
        return manufacturerRepository.findByCategoriesAndLanguage(ids, language.getId());
    }

    @Override
    public void addManufacturerDescription(Manufacturer manufacturer,
                                           ManufacturerDescription description) throws ServiceException {


        if (manufacturer.getDescriptions() == null) {
            manufacturer.setDescriptions(new HashSet<ManufacturerDescription>());
        }

        manufacturer.getDescriptions().add(description);
        description.setManufacturer(manufacturer);
        update(manufacturer);
    }

    @Override
    public void saveOrUpdate(Manufacturer manufacturer) throws ServiceException {

        LOGGER.debug("Creating Manufacturer");

        if (manufacturer.getId() != null && manufacturer.getId() > 0) {
            super.update(manufacturer);

        } else {
            super.create(manufacturer);

        }
    }

    @Override
    public Manufacturer getByCode(com.kamatchibotique.application.model.merchant.MerchantStore store,
                                  String code) {
        return manufacturerRepository.findByCodeAndMerchandStore(code, store.getId());
    }

    @Override
    public Manufacturer getById(Long id) {
        return manufacturerRepository.findOne(id);
    }

    @Override
    public List<Manufacturer> listByProductsInCategory(MerchantStore store, Category category,
                                                       Language language) throws ServiceException {
        Objects.requireNonNull(store, "Store cannot be null");
        Objects.requireNonNull(category, "Category cannot be null");
        Objects.requireNonNull(language, "Language cannot be null");
        return manufacturerRepository.findByProductInCategoryId(store.getId(), category.getLineage(), language.getId());
    }

    @Override
    public Page<Manufacturer> listByStore(MerchantStore store, Language language, int page, int count)
            throws ServiceException {

        Pageable pageRequest = PageRequest.of(page, count);
        return pageableManufacturerRepository.findByStore(store.getId(), language.getId(), null, pageRequest);
    }

    @Override
    public int count(MerchantStore store) {
        Objects.requireNonNull(store, "Merchant must not be null");
        return manufacturerRepository.count(store.getId());
    }

    @Override
    public Page<Manufacturer> listByStore(MerchantStore store, Language language, String name,
                                          int page, int count) throws ServiceException {

        Pageable pageRequest = PageRequest.of(page, count);
        return pageableManufacturerRepository.findByStore(store.getId(), language.getId(), name, pageRequest);
    }

    @Override
    public Page<Manufacturer> listByStore(MerchantStore store, String name, int page, int count)
            throws ServiceException {

        Pageable pageRequest = PageRequest.of(page, count);
        return pageableManufacturerRepository.findByStore(store.getId(), name, pageRequest);
    }
}
