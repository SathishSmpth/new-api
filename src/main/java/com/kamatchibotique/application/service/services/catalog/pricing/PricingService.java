package com.kamatchibotique.application.service.services.catalog.pricing;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductAttribute;
import com.kamatchibotique.application.model.catalog.product.availability.ProductAvailability;
import com.kamatchibotique.application.model.catalog.product.price.FinalPrice;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariant;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.currency.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public interface PricingService {

    FinalPrice calculateProductPrice(Product product) throws ServiceException;

    FinalPrice calculateProductPrice(ProductVariant variant) throws ServiceException;

    FinalPrice calculateProductPrice(ProductAvailability product) throws ServiceException;

    FinalPrice calculateProductPrice(Product product, Customer customer)
            throws ServiceException;

    FinalPrice calculateProductPrice(Product product,
                                     List<ProductAttribute> attributes) throws ServiceException;

    FinalPrice calculateProductPrice(Product product,
                                     List<ProductAttribute> attributes, Customer customer)
            throws ServiceException;

    String getDisplayAmount(BigDecimal amount, MerchantStore store)
            throws ServiceException;

    String getDisplayAmount(BigDecimal amount, Locale locale, Currency currency, MerchantStore store)
            throws ServiceException;

    BigDecimal getAmount(String amount) throws ServiceException;

    String getStringAmount(BigDecimal amount, MerchantStore store)
            throws ServiceException;

    BigDecimal calculatePriceQuantity(BigDecimal price, int quantity);
}
