package com.kamatchibotique.application.model.shoppingcart;

import com.kamatchibotique.application.model.common.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductAttribute;


@Entity
@Table(name = "SHOPPING_CART_ATTR_ITEM")
public class ShoppingCartAttributeItem extends Auditable<String> {


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SHP_CART_ATTR_ITEM_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "SHP_CRT_ATTR_ITM_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	
	@Column(name="PRODUCT_ATTR_ID", nullable=false)
	private Long productAttributeId;
	
	@JsonIgnore
	@Transient
	private ProductAttribute productAttribute;
	

	@JsonIgnore
	@ManyToOne(targetEntity = ShoppingCartItem.class)
	@JoinColumn(name = "SHP_CART_ITEM_ID", nullable = false)
	private ShoppingCartItem shoppingCartItem;
	
	public ShoppingCartAttributeItem(ShoppingCartItem shoppingCartItem, ProductAttribute productAttribute) {
		this.shoppingCartItem = shoppingCartItem;
		this.productAttribute = productAttribute;
		this.productAttributeId = productAttribute.getId();
	}
	
	public ShoppingCartAttributeItem(ShoppingCartItem shoppingCartItem, Long productAttributeId) {
		this.shoppingCartItem = shoppingCartItem;
		this.productAttributeId = productAttributeId;
	}
	
	public ShoppingCartAttributeItem() {
		
	}


	public ShoppingCartItem getShoppingCartItem() {
		return shoppingCartItem;
	}

	public void setShoppingCartItem(ShoppingCartItem shoppingCartItem) {
		this.shoppingCartItem = shoppingCartItem;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
		
	}


	public void setProductAttributeId(Long productAttributeId) {
		this.productAttributeId = productAttributeId;
	}

	public Long getProductAttributeId() {
		return productAttributeId;
	}

	public void setProductAttribute(ProductAttribute productAttribute) {
		this.productAttribute = productAttribute;
	}

	public ProductAttribute getProductAttribute() {
		return productAttribute;
	}


}
