package com.kamatchibotique.application.service.services.shoppingcart;

import java.util.List;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.shipping.ShippingProduct;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCart;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;

public interface ShoppingCartService extends CommonService<Long, ShoppingCart> {

	ShoppingCart getShoppingCart(Customer customer, MerchantStore store) throws ServiceException;

	void saveOrUpdate(ShoppingCart shoppingCart) throws ServiceException;

	ShoppingCart getById(Long id, MerchantStore store) throws ServiceException;

	ShoppingCart getByCode(String code, MerchantStore store) throws ServiceException;

	List<ShippingProduct> createShippingProduct(ShoppingCart cart) throws ServiceException;

	ShoppingCartItem populateShoppingCartItem(Product product, MerchantStore store) throws ServiceException;

	void deleteCart(ShoppingCart cart) throws ServiceException;

	void removeShoppingCart(ShoppingCart cart) throws ServiceException;

	ShoppingCart mergeShoppingCarts(final ShoppingCart userShoppingCart, final ShoppingCart sessionCart,
			final MerchantStore store) throws Exception;

	void deleteShoppingCartItem(Long id);

}