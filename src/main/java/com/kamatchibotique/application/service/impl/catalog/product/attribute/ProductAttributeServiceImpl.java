package com.kamatchibotique.application.service.impl.catalog.product.attribute;

import java.util.List;

import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.service.services.catalog.product.attribute.ProductAttributeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.repository.catalog.product.attribute.PageableProductAttributeRepository;
import com.kamatchibotique.application.repository.catalog.product.attribute.ProductAttributeRepository;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductAttribute;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

@Service("productAttributeService")
public class ProductAttributeServiceImpl extends CommonServiceImpl<Long, ProductAttribute>
		implements ProductAttributeService {

	private ProductAttributeRepository productAttributeRepository;
	@Autowired
	private PageableProductAttributeRepository pageableProductAttributeRepository;

	@Autowired
	public ProductAttributeServiceImpl(ProductAttributeRepository productAttributeRepository) {
		super(productAttributeRepository);
		this.productAttributeRepository = productAttributeRepository;
	}

	@Override
	public ProductAttribute getById(Long id) {

		return productAttributeRepository.findOne(id);

	}

	@Override
	public List<ProductAttribute> getByOptionId(MerchantStore store, Long id) throws ServiceException {

		return productAttributeRepository.findByOptionId(store.getId(), id);

	}

	@Override
	public List<ProductAttribute> getByAttributeIds(MerchantStore store, Product product, List<Long> ids)
			throws ServiceException {

		return productAttributeRepository.findByAttributeIds(store.getId(), product.getId(), ids);

	}

	@Override
	public List<ProductAttribute> getByOptionValueId(MerchantStore store, Long id) throws ServiceException {

		return productAttributeRepository.findByOptionValueId(store.getId(), id);

	}

	/**
	 * Returns all product attributes
	 */
	@Override
	public Page<ProductAttribute> getByProductId(MerchantStore store, Product product, Language language, int page,
			int count) throws ServiceException {

		Pageable pageRequest = PageRequest.of(page, count);
		return pageableProductAttributeRepository.findByProductId(store.getId(), product.getId(), language.getId(),
				pageRequest);

	}

	@Override
	public Page<ProductAttribute> getByProductId(MerchantStore store, Product product, int page, int count)
			throws ServiceException {
		Pageable pageRequest = PageRequest.of(page, count);
		return pageableProductAttributeRepository.findByProductId(store.getId(), product.getId(), pageRequest);

	}

	@Override
	public ProductAttribute saveOrUpdate(ProductAttribute productAttribute) throws ServiceException {
		productAttribute = productAttributeRepository.save(productAttribute);
		return productAttribute;

	}

	@Override
	public void delete(ProductAttribute attribute) throws ServiceException {

		// override method, this allows the error that we try to remove a detached
		// variant
		attribute = this.getById(attribute.getId());
		super.delete(attribute);

	}

	@Override
	public List<ProductAttribute> getProductAttributesByCategoryLineage(MerchantStore store, String lineage,
			Language language) throws Exception {
		return productAttributeRepository.findOptionsByCategoryLineage(store.getId(), lineage, language.getId());
	}

}
