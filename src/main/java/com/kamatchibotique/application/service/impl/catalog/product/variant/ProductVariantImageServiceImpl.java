package com.kamatchibotique.application.service.impl.catalog.product.variant;

import java.util.List;
import java.util.Objects;

import com.kamatchibotique.application.service.services.catalog.product.variant.ProductVariantImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.repository.catalog.product.variant.ProductVariantImageRepository;
import com.kamatchibotique.application.service.impl.common.CommonServiceImpl;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariantImage;
import com.kamatchibotique.application.model.merchant.MerchantStore;


@Service("productVariantImageService")
public class ProductVariantImageServiceImpl extends CommonServiceImpl<Long, ProductVariantImage> implements ProductVariantImageService {

	@Autowired
	private ProductVariantImageRepository productVariantImageRepository;
	
	public ProductVariantImageServiceImpl(ProductVariantImageRepository productVariantImageRepository) {
		super(productVariantImageRepository);
		this.productVariantImageRepository = productVariantImageRepository;
	}

	@Override
	public List<ProductVariantImage> list(Long productVariantId, MerchantStore store) {
		Objects.requireNonNull(store, "MerchantStore cannot be null");
		return productVariantImageRepository.finByProductVariant(productVariantId, store.getCode());
	}

	@Override
	public List<ProductVariantImage> listByProduct(Long productId, MerchantStore store) {
		Objects.requireNonNull(store, "MerchantStore cannot be null");
		return productVariantImageRepository.finByProduct(productId, store.getCode());
	}

	@Override
	public List<ProductVariantImage> listByProductVariantGroup(Long productVariantGroupId, MerchantStore store) {
		Objects.requireNonNull(store, "MerchantStore cannot be null");
		return productVariantImageRepository.finByProductVariantGroup(productVariantGroupId, store.getCode());
	}

}
