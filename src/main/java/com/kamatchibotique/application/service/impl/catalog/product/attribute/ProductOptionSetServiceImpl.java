package com.kamatchibotique.application.service.impl.catalog.product.attribute;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductOptionSet;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.repository.catalog.product.attribute.ProductOptionSetRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.catalog.product.attribute.ProductOptionSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productOptionSetService")
public class ProductOptionSetServiceImpl extends
        CommonServiceImpl<Long, ProductOptionSet> implements ProductOptionSetService {


    private ProductOptionSetRepository productOptionSetRepository;


    @Autowired
    public ProductOptionSetServiceImpl(
            ProductOptionSetRepository productOptionSetRepository) {
        super(productOptionSetRepository);
        this.productOptionSetRepository = productOptionSetRepository;
    }


    @Override
    public List<ProductOptionSet> listByStore(MerchantStore store, Language language) throws ServiceException {
        return productOptionSetRepository.findByStore(store.getId(), language.getId());
    }


    @Override
    public ProductOptionSet getById(MerchantStore store, Long optionSetId, Language lang) {
        return productOptionSetRepository.findOne(store.getId(), optionSetId, lang.getId());
    }


    @Override
    public ProductOptionSet getCode(MerchantStore store, String code) {
        return productOptionSetRepository.findByCode(store.getId(), code);
    }


    @Override
    public List<ProductOptionSet> getByProductType(Long productTypeId, MerchantStore store, Language lang) {
        return productOptionSetRepository.findByProductType(productTypeId, store.getId(), lang.getId());
    }
}
