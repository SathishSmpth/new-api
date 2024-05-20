package com.kamatchibotique.application.service.impl.catalog.product.variation;

import java.util.List;
import java.util.Optional;

import com.kamatchibotique.application.service.services.catalog.product.variation.ProductVariationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.catalog.product.variation.PageableProductVariationRepository;
import com.kamatchibotique.application.repository.catalog.product.variation.ProductVariationRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.catalog.product.variation.ProductVariation;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

@Service("productVariationeService")
public class ProductVariationServiceImpl extends
		CommonServiceImpl<Long, ProductVariation> implements
        ProductVariationService {

	@Autowired
	private ProductVariationRepository productVariationRepository;
	
	@Autowired
	public ProductVariationServiceImpl(
			ProductVariationRepository productVariationSetRepository) {
		super(productVariationSetRepository);
		this.productVariationRepository = productVariationSetRepository;
	}


	@Autowired
	private PageableProductVariationRepository pageableProductVariationSetRepository;


	@Override
	public Optional<ProductVariation> getById(MerchantStore store, Long id, Language lang) {
		return productVariationRepository.findOne(store.getId(), id, lang.getId());
	}
	
	@Override
	public Optional<ProductVariation> getByCode(MerchantStore store, String code) {
		return productVariationRepository.findByCode(code, store.getId());
	}



	@Override
	public Page<ProductVariation> getByMerchant(MerchantStore store, Language language, String code, int page,
			int count) {
		Pageable p = PageRequest.of(page, count);
		return pageableProductVariationSetRepository.list(store.getId(), code, p);
	}

	@Override
	public Optional<ProductVariation> getById(MerchantStore store, Long id) {
		return productVariationRepository.findOne(store.getId(), id);
	}
	
	@Override
	public void saveOrUpdate(ProductVariation entity) throws ServiceException {

		//save or update (persist and attach entities
		if(entity.getId()!=null && entity.getId()>0) {

			super.update(entity);
			
		} else {
			
			super.save(entity);
			
		}
		
	}

	@Override
	public List<ProductVariation> getByIds(List<Long> ids, MerchantStore store) {
		return productVariationRepository.findByIds(store.getId(), ids);
	}



}
