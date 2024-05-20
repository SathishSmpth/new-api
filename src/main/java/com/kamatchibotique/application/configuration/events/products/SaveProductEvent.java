package com.kamatchibotique.application.configuration.events.products;

import com.kamatchibotique.application.model.catalog.product.Product;

public class SaveProductEvent extends ProductEvent {
	
	public SaveProductEvent(Object source, Product product) {
		super(source, product);
	}

	private static final long serialVersionUID = 1L;
	
	
	

}
