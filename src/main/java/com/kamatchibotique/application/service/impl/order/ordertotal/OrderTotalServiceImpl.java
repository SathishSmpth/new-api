package com.kamatchibotique.application.service.impl.order.ordertotal;

import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.OrderSummary;
import com.kamatchibotique.application.model.order.OrderTotal;
import com.kamatchibotique.application.model.order.OrderTotalVariation;
import com.kamatchibotique.application.model.order.RebatesOrderTotalVariation;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;
import com.kamatchibotique.application.order.total.OrderTotalPostProcessorModule;
import com.kamatchibotique.application.service.services.catalog.product.ProductService;
import com.kamatchibotique.application.service.services.order.ordertotal.OrderTotalService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("OrderTotalService")
public class OrderTotalServiceImpl implements OrderTotalService {

    @Autowired
    @Resource(name = "orderTotalsPostProcessors")
    List<OrderTotalPostProcessorModule> orderTotalPostProcessors;

    @Autowired
    private ProductService productService;


    @Override
    public OrderTotalVariation findOrderTotalVariation(OrderSummary summary, Customer customer, MerchantStore store, Language language)
            throws Exception {

        RebatesOrderTotalVariation variation = new RebatesOrderTotalVariation();

        List<OrderTotal> totals = null;

        if (orderTotalPostProcessors != null) {
            for (OrderTotalPostProcessorModule module : orderTotalPostProcessors) {
                List<ShoppingCartItem> items = summary.getProducts();
                for (ShoppingCartItem item : items) {

                    Product product = productService.getBySku(item.getSku(), store, language);

                    OrderTotal orderTotal = module.caculateProductPiceVariation(summary, item, product, customer, store);
                    if (orderTotal == null) {
                        continue;
                    }
                    if (totals == null) {
                        totals = new ArrayList<OrderTotal>();
                        variation.setVariations(totals);
                    }

                    //if product is null it will be catched when invoking the module
                    orderTotal.setText(StringUtils.isNoneBlank(orderTotal.getText()) ? orderTotal.getText() : product.getProductDescription().getName());
                    variation.getVariations().add(orderTotal);
                }
            }
        }
        return variation;
    }

}
