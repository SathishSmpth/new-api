package com.kamatchibotique.application.order.total;

import com.kamatchibotique.application.configuration.DroolsBeanFactory;
import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.enums.order.OrderTotalType;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.price.FinalPrice;
import com.kamatchibotique.application.model.customer.Customer;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.order.OrderSummary;
import com.kamatchibotique.application.model.order.OrderTotal;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;
import com.kamatchibotique.application.service.services.catalog.pricing.PricingService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class PromoCodeCalculatorModule implements OrderTotalPostProcessorModule {


    @Autowired
    private DroolsBeanFactory droolsBeanFactory;

    @Autowired
    private PricingService pricingService;

    private String name;
    private String code;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;

    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public OrderTotal caculateProductPiceVariation(OrderSummary summary, ShoppingCartItem shoppingCartItem,
                                                   Product product, Customer customer, MerchantStore store) throws Exception {

        Validate.notNull(summary, "OrderTotalSummary must not be null");
        Validate.notNull(store, "MerchantStore must not be null");

        if (StringUtils.isBlank(summary.getPromoCode())) {
            return null;
        }

        KieSession kieSession = droolsBeanFactory.getKieSession(ResourceFactory.newClassPathResource("com/salesmanager/drools/rules/PromoCoupon.drl"));

        com.kamatchibotique.application.order.total.OrderTotalResponse resp = new OrderTotalResponse();

        OrderTotalInputParameters inputParameters = new OrderTotalInputParameters();
        inputParameters.setPromoCode(summary.getPromoCode());
        inputParameters.setDate(new Date());

        kieSession.insert(inputParameters);
        kieSession.setGlobal("total", resp);
        kieSession.fireAllRules();

        if (resp.getDiscount() != null) {

            OrderTotal orderTotal = null;
            if (resp.getDiscount() != null) {
                orderTotal = new OrderTotal();
                orderTotal.setOrderTotalCode(Constants.OT_DISCOUNT_TITLE);
                orderTotal.setOrderTotalType(OrderTotalType.SUBTOTAL);
                orderTotal.setTitle(Constants.OT_SUBTOTAL_MODULE_CODE);
                orderTotal.setText(summary.getPromoCode());

                //calculate discount that will be added as a negative value
                FinalPrice productPrice = pricingService.calculateProductPrice(product);

                Double discount = resp.getDiscount();
                BigDecimal reduction = productPrice.getFinalPrice().multiply(new BigDecimal(discount));
                reduction = reduction.multiply(new BigDecimal(shoppingCartItem.getQuantity()));

                orderTotal.setValue(reduction);//discount value

                //TODO check expiration
            }


            return orderTotal;

        }


        return null;
    }

}
