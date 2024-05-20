package com.kamatchibotique.application.service.impl.catalog.pricing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import com.kamatchibotique.application.service.services.catalog.pricing.PricingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.utils.ProductPriceUtils;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.attribute.ProductAttribute;
import com.kamatchibotique.application.model.catalog.product.availability.ProductAvailability;
import com.kamatchibotique.application.model.catalog.product.price.FinalPrice;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariant;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.currency.Currency;

@Service("pricingService")
public class PricingServiceImpl implements PricingService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PricingServiceImpl.class);
	

	@Autowired
	private ProductPriceUtils priceUtil;
	
	@Override
	public FinalPrice calculateProductPrice(Product product) throws ServiceException {
		return priceUtil.getFinalPrice(product);
	}
	
	@Override
	public FinalPrice calculateProductPrice(Product product, Customer customer) throws ServiceException {
		/** TODO add rules for price calculation **/
		return priceUtil.getFinalPrice(product);
	}
	
	@Override
	public FinalPrice calculateProductPrice(Product product, List<ProductAttribute> attributes) throws ServiceException {
		return priceUtil.getFinalPrice(product, attributes);
	}
	
	@Override
	public FinalPrice calculateProductPrice(Product product, List<ProductAttribute> attributes, Customer customer) throws ServiceException {
		/** TODO add rules for price calculation **/
		return priceUtil.getFinalPrice(product, attributes);
	}
	
	@Override
	public BigDecimal calculatePriceQuantity(BigDecimal price, int quantity) {
		return price.multiply(new BigDecimal(quantity));
	}

	@Override
	public String getDisplayAmount(BigDecimal amount, MerchantStore store) throws ServiceException {
		try {
			return priceUtil.getStoreFormatedAmountWithCurrency(store,amount);
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount.toString());
			throw new ServiceException(e);
		}
	}
	
	@Override
	public String getDisplayAmount(BigDecimal amount, Locale locale,
			Currency currency, MerchantStore store) throws ServiceException {
		try {
			return priceUtil.getFormatedAmountWithCurrency(locale, currency, amount);
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amunt " + amount.toString() + " using locale " + locale.toString() + " and currency " + currency.toString());
			throw new ServiceException(e);
		}
	}

	@Override
	public String getStringAmount(BigDecimal amount, MerchantStore store)
			throws ServiceException {
		try {
			return priceUtil.getAdminFormatedAmount(store, amount);
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount.toString());
			throw new ServiceException(e);
		}
	}

	@Override
	public BigDecimal getAmount(String amount) throws ServiceException {

		try {
			return priceUtil.getAmount(amount);
		} catch (Exception e) {
			LOGGER.error("An error occured when trying to format an amount " + amount);
			throw new ServiceException(e);
		}

	}

	@Override
	public FinalPrice calculateProductPrice(ProductAvailability availability) throws ServiceException {

		return priceUtil.getFinalPrice(availability);
	}

	@Override
	public FinalPrice calculateProductPrice(ProductVariant variant) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}


	
}
