package com.kamatchibotique.application.configuration.events.products;

import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariant;

public class SaveProductVariantEvent extends ProductEvent {
	
	private static final long serialVersionUID = 1L;
	private ProductVariant variant;

	public SaveProductVariantEvent(Object source, ProductVariant variant, Product product) {
		super(source, product);
		this.variant = variant;
	}

	public ProductVariant getVariant() {
		return variant;
	}


}
