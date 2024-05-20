package com.kamatchibotique.application.order.total;


import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.OrderSummary;
import com.kamatchibotique.application.model.order.OrderTotal;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;
import com.kamatchibotique.application.order.Module;

public interface OrderTotalPostProcessorModule extends Module {
    OrderTotal caculateProductPiceVariation(final OrderSummary summary, final ShoppingCartItem shoppingCartItem, final Product product, final Customer customer, final MerchantStore store) throws Exception;
}
