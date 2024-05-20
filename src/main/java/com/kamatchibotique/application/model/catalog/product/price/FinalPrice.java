package com.kamatchibotique.application.model.catalog.product.price;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class FinalPrice implements Serializable {
    private static final long serialVersionUID = 1L;
    private BigDecimal discountedPrice = null;
    private BigDecimal originalPrice = null;
    private BigDecimal finalPrice = null;
    private boolean discounted = false;
    private int discountPercent = 0;
    private String stringPrice;
    private String stringDiscountedPrice;
    private Date discountEndDate = null;
    private boolean defaultPrice;
    private ProductPrice productPrice;
    List<FinalPrice> additionalPrices;
}
