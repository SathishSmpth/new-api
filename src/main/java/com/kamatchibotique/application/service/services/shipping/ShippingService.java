package com.kamatchibotique.application.service.services.shipping;

import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.common.Delivery;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.country.Country;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.model.shipping.*;
import com.kamatchibotique.application.model.shoppingcart.ShoppingCartItem;
import com.kamatchibotique.application.model.system.CustomIntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationModule;

import java.util.List;
import java.util.Map;


public interface ShippingService {

    List<String> getSupportedCountries(MerchantStore store)
            throws ServiceException;

    void setSupportedCountries(MerchantStore store,
                               List<String> countryCodes) throws ServiceException;

    List<IntegrationModule> getShippingMethods(MerchantStore store)
            throws ServiceException;

    Map<String, IntegrationConfiguration> getShippingModulesConfigured(
            MerchantStore store) throws ServiceException;

    void saveShippingQuoteModuleConfiguration(IntegrationConfiguration configuration,
                                              MerchantStore store) throws ServiceException;

    ShippingConfiguration getShippingConfiguration(MerchantStore store)
            throws ServiceException;

    void saveShippingConfiguration(ShippingConfiguration shippingConfiguration,
                                   MerchantStore store) throws ServiceException;

    void removeShippingQuoteModuleConfiguration(String moduleCode,
                                                MerchantStore store) throws ServiceException;

    List<PackageDetails> getPackagesDetails(List<ShippingProduct> products,
                                            MerchantStore store) throws ServiceException;

    ShippingQuote getShippingQuote(Long shoppingCartId, MerchantStore store, Delivery delivery,
                                   List<ShippingProduct> products, Language language)
            throws ServiceException;

    IntegrationConfiguration getShippingConfiguration(String moduleCode,
                                                      MerchantStore store) throws ServiceException;

    CustomIntegrationConfiguration getCustomShippingConfiguration(
            String moduleCode, MerchantStore store) throws ServiceException;

    void saveCustomShippingConfiguration(String moduleCode,
                                         CustomIntegrationConfiguration shippingConfiguration,
                                         MerchantStore store) throws ServiceException;

    void removeCustomShippingQuoteModuleConfiguration(String moduleCode,
                                                      MerchantStore store) throws ServiceException;

    ShippingSummary getShippingSummary(MerchantStore store, ShippingQuote shippingQuote,
                                       ShippingOption selectedShippingOption) throws ServiceException;

    List<Country> getShipToCountryList(MerchantStore store, Language language)
            throws ServiceException;

    boolean requiresShipping(List<ShoppingCartItem> items, MerchantStore store) throws ServiceException;

    ShippingMetaData getShippingMetaData(MerchantStore store) throws ServiceException;

    boolean hasTaxOnShipping(MerchantStore store) throws ServiceException;
}