package com.kamatchibotique.application.service.impl.catalog.product.attribute;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductAttribute;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductOption;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.repository.catalog.product.attribute.PageableProductOptionRepository;
import com.kamatchibotique.application.repository.catalog.product.attribute.ProductOptionRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.catalog.product.attribute.ProductAttributeService;
import com.kamatchibotique.application.service.services.catalog.product.attribute.ProductOptionService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service("productOptionService")
public class ProductOptionServiceImpl extends
        CommonServiceImpl<Long, ProductOption> implements ProductOptionService {


    private ProductOptionRepository productOptionRepository;

    @Autowired
    private PageableProductOptionRepository pageableProductOptionRepository;

    @Autowired
    private ProductAttributeService productAttributeService;

    @Autowired
    public ProductOptionServiceImpl(
            ProductOptionRepository productOptionRepository) {
        super(productOptionRepository);
        this.productOptionRepository = productOptionRepository;
    }

    @Override
    public List<ProductOption> listByStore(MerchantStore store, Language language) throws ServiceException {


        return productOptionRepository.findByStoreId(store.getId(), language.getId());


    }

    @Override
    public List<ProductOption> listReadOnly(MerchantStore store, Language language) throws ServiceException {

        return productOptionRepository.findByReadOnly(store.getId(), language.getId(), true);


    }


    @Override
    public List<ProductOption> getByName(MerchantStore store, String name, Language language) throws ServiceException {

        try {
            return productOptionRepository.findByName(store.getId(), name, language.getId());
        } catch (Exception e) {
            throw new ServiceException(e);
        }


    }

    @Override
    public void saveOrUpdate(ProductOption entity) throws ServiceException {


        //save or update (persist and attach entities
        if (entity.getId() != null && entity.getId() > 0) {
            super.update(entity);
        } else {
            super.save(entity);
        }

    }

    @Override
    public void delete(ProductOption entity) throws ServiceException {

        //remove all attributes having this option
        List<ProductAttribute> attributes = productAttributeService.getByOptionId(entity.getMerchantStore(), entity.getId());

        for (ProductAttribute attribute : attributes) {
            productAttributeService.delete(attribute);
        }

        ProductOption option = this.getById(entity.getId());

        //remove option
        super.delete(option);

    }

    @Override
    public ProductOption getByCode(MerchantStore store, String optionCode) {
        return productOptionRepository.findByCode(store.getId(), optionCode);
    }

    @Override
    public ProductOption getById(MerchantStore store, Long optionId) {
        return productOptionRepository.findOne(store.getId(), optionId);
    }

    @Override
    public Page<ProductOption> getByMerchant(MerchantStore store, Language language, String name,
                                             int page, int count) {
       Objects.requireNonNull(store, "MerchantStore cannot be null");
        Pageable p = PageRequest.of(page, count);
        return pageableProductOptionRepository.listOptions(store.getId(), name, p);
    }


}
