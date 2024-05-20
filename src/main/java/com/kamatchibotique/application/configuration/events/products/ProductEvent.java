package com.kamatchibotique.application.configuration.events.products;

import org.springframework.context.ApplicationEvent;

import com.kamatchibotique.application.model.catalog.product.Product;

public abstract class ProductEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;
	private Product product;
	
	public ProductEvent(Object source, Product product) {
		super(source);
		this.product = product;
	}


	public Product getProduct() {
		return product;
	}

}
