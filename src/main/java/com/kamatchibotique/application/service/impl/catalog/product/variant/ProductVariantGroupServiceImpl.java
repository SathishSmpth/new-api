package com.kamatchibotique.application.service.impl.catalog.product.variant;

import java.util.Optional;

import com.kamatchibotique.application.service.services.catalog.product.variant.ProductVariantGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.catalog.product.variant.PageableProductVariantGroupRepository;
import com.kamatchibotique.application.repository.catalog.product.variant.ProductVariantGroupRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariantGroup;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;


@Service("productVariantGroupService")
public class ProductVariantGroupServiceImpl extends CommonServiceImpl<Long, ProductVariantGroup> implements ProductVariantGroupService {

	
	@Autowired
	private PageableProductVariantGroupRepository pageableProductVariantGroupRepository;
	
	private ProductVariantGroupRepository productVariantGroupRepository;
	
	public ProductVariantGroupServiceImpl(ProductVariantGroupRepository repository) {
		super(repository);
		this.productVariantGroupRepository = repository;
	}

	@Override
	public Optional<ProductVariantGroup> getById(Long id, MerchantStore store) {
		return  productVariantGroupRepository.findOne(id, store.getCode());

	}

	@Override
	public Optional<ProductVariantGroup> getByProductVariant(Long productVariantId, MerchantStore store,
			Language language) {
		return productVariantGroupRepository.finByProductVariant(productVariantId, store.getCode());
	}

	@Override
	public ProductVariantGroup saveOrUpdate(ProductVariantGroup entity) throws ServiceException {
		
		entity = productVariantGroupRepository.save(entity);
		return entity;
		
	}

	@Override
	public Page<ProductVariantGroup> getByProductId(MerchantStore store, Long productId, Language language, int page,
			int count) {
		
		Pageable pageRequest = PageRequest.of(page, count);
		return pageableProductVariantGroupRepository.findByProductId(store.getId(), productId, pageRequest);
	}


}
