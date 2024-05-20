package com.kamatchibotique.application.service.impl.catalog.inventory;

import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.catalog.product.Product;
import com.kamatchibotique.application.model.catalog.product.availability.ProductAvailability;
import com.kamatchibotique.application.model.catalog.product.inventory.ProductInventory;
import com.kamatchibotique.application.model.catalog.product.price.FinalPrice;
import com.kamatchibotique.application.model.catalog.product.variant.ProductVariant;
import com.kamatchibotique.application.service.services.catalog.inventory.ProductInventoryService;
import com.kamatchibotique.application.service.services.catalog.pricing.PricingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Set;


@Service("inventoryService")
public class ProductInventoryServiceImpl implements ProductInventoryService {


    @Autowired
    private PricingService pricingService;

    @Override
    public ProductInventory inventory(Product product) throws ServiceException {
        Objects.requireNonNull(product.getAvailabilities());

        ProductAvailability availability = defaultAvailability(product.getAvailabilities());
        FinalPrice finalPrice = pricingService.calculateProductPrice(product);

        ProductInventory inventory = inventory(availability, finalPrice);
        inventory.setSku(product.getSku());
        return inventory;
    }

    @Override
    public ProductInventory inventory(ProductVariant variant) throws ServiceException {
        Objects.requireNonNull(variant.getAvailabilities());
        Objects.requireNonNull(variant.getProduct());

        ProductAvailability availability = null;
        if (!CollectionUtils.isEmpty(variant.getAvailabilities())) {
            availability = defaultAvailability(variant.getAvailabilities());
        } else {
            availability = defaultAvailability(variant.getProduct().getAvailabilities());
        }
        FinalPrice finalPrice = pricingService.calculateProductPrice(variant);

        if (finalPrice == null) {
            finalPrice = pricingService.calculateProductPrice(variant.getProduct());
        }

        ProductInventory inventory = inventory(availability, finalPrice);
        inventory.setSku(variant.getSku());
        return inventory;
    }

    private ProductAvailability defaultAvailability(Set<ProductAvailability> availabilities) {

        ProductAvailability defaultAvailability = availabilities.iterator().next();

        for (ProductAvailability availability : availabilities) {
            if (!StringUtils.isEmpty(availability.getRegion())
                    && availability.getRegion().equals(Constants.ALL_REGIONS)) {// TODO REL 2.1 accept a region
                defaultAvailability = availability;
            }
        }

        return defaultAvailability;

    }

    private ProductInventory inventory(ProductAvailability availability, FinalPrice price) {
        ProductInventory inventory = new ProductInventory();
        inventory.setQuantity(availability.getProductQuantity());
        inventory.setPrice(price);

        return inventory;
    }

}
