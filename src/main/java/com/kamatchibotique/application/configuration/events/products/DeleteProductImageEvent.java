package com.kamatchibotique.application.configuration.events.products;

import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.image.ProductImage;

public class DeleteProductImageEvent extends ProductEvent {
	
	
	private static final long serialVersionUID = 1L;
	private ProductImage productImage;

	public ProductImage getProductImage() {
		return productImage;
	}

	public DeleteProductImageEvent(Object source, ProductImage productImage, Product product) {
		super(source, product);
		this.productImage = productImage;

	}


}
