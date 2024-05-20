package com.kamatchibotique.application.service.impl.catalog.product.type;

import java.util.List;

import com.kamatchibotique.application.service.services.catalog.product.type.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.catalog.product.type.PageableProductTypeRepository;
import com.kamatchibotique.application.repository.catalog.product.type.ProductTypeRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.catalog.product.type.ProductType;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

@Service("productTypeService")
public class ProductTypeServiceImpl extends CommonServiceImpl<Long, ProductType>
		implements ProductTypeService {

	private ProductTypeRepository productTypeRepository;
	
	@Autowired
	private PageableProductTypeRepository pageableProductTypeRepository;

	@Autowired
	public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
		super(productTypeRepository);
		this.productTypeRepository = productTypeRepository;
	}

	@Override
	public ProductType getByCode(String code, MerchantStore store, Language language) throws ServiceException {
		return productTypeRepository.findByCode(code, store.getId());
	}

	@Override
	public void update(String code, MerchantStore store, ProductType type) throws ServiceException {
		productTypeRepository.save(type);

	}
	
	@Override
	public ProductType getProductType(String productTypeCode) {
		return productTypeRepository.findByCode(productTypeCode);
	}

	@Override
	public Page<ProductType> getByMerchant(MerchantStore store, Language language, int page, int count) throws ServiceException {
		Pageable pageRequest = PageRequest.of(page, count);
		return pageableProductTypeRepository.listByStore(store.getId(), pageRequest);
	}

	@Override
	public ProductType getById(Long id, MerchantStore store, Language language) throws ServiceException {
		return productTypeRepository.findById(id, store.getId(), language.getId());
	}

	@Override
	public ProductType saveOrUpdate(ProductType productType) throws ServiceException {
		if(productType.getId()!=null && productType.getId() > 0) {
			this.update(productType);
		} else {
			productType = super.saveAndFlush(productType);
		}
		
		return productType;
	}

	@Override
	public List<ProductType> listProductTypes(List<Long> ids, MerchantStore store, Language language)
			throws ServiceException {
		return productTypeRepository.findByIds(ids, store.getId(), language.getId());
	}

	@Override
	public ProductType getById(Long id, MerchantStore store) throws ServiceException {
		return productTypeRepository.findById(id, store.getId());
	}


}
