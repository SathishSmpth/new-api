package com.kamatchibotique.application.integration.shipping.impl;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.*;
import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.integration.IntegrationExceptionService;
import com.kamatchibotique.application.integration.shipping.model.ShippingQuotePrePostProcessModule;
import com.kamatchibotique.application.model.common.Delivery;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.shipping.PackageDetails;
import com.kamatchibotique.application.model.shipping.ShippingConfiguration;
import com.kamatchibotique.application.model.shipping.ShippingOrigin;
import com.kamatchibotique.application.model.shipping.ShippingQuote;
import com.kamatchibotique.application.model.system.IntegrationConfiguration;
import com.kamatchibotique.application.model.system.IntegrationModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Component("shippingDistancePreProcessor")
public class ShippingDistancePreProcessorImpl implements ShippingQuotePrePostProcessModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShippingDistancePreProcessorImpl.class);

    private static final String BLANK = " ";

    private static final String MODULE_CODE = "shippingDistanceModule";

    @Value("${config.shippingDistancePreProcessor.apiKey}")
    private String apiKey;

    @Value("#{'${config.shippingDistancePreProcessor.acceptedZones}'.split(',')}")
    private List<String> allowedZonesCodes = null;

    public List<String> getAllowedZonesCodes() {
        return allowedZonesCodes;
    }

    public void setAllowedZonesCodes(List<String> allowedZonesCodes) {
        this.allowedZonesCodes = allowedZonesCodes;
    }

    public void prePostProcessShippingQuotes(ShippingQuote quote,
                                             List<PackageDetails> packages, BigDecimal orderTotal,
                                             Delivery delivery, ShippingOrigin origin, MerchantStore store,
                                             IntegrationConfiguration globalShippingConfiguration,
                                             IntegrationModule currentModule,
                                             ShippingConfiguration shippingConfiguration,
                                             List<IntegrationModule> allModules, Locale locale)
            throws IntegrationExceptionService {

        /** which destinations are supported by this module **/

        if (delivery.getZone() == null) {
            return;
        }

        boolean zoneAllowed = false;
        if (allowedZonesCodes != null) {
            for (String zoneCode : allowedZonesCodes) {
                if (zoneCode.equals(delivery.getZone().getCode())) {
                    zoneAllowed = true;
                    break;
                }
            }
        }

        if (!zoneAllowed) {
            return;
        }

        if (StringUtils.isBlank(delivery.getPostalCode())) {
            return;
        }

        Validate.notNull(apiKey, "Requires the configuration of google apiKey");

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        // Build origin address
        StringBuilder originAddress = new StringBuilder();

        originAddress.append(origin.getAddress()).append(BLANK)
                .append(origin.getCity()).append(BLANK)
                .append(origin.getPostalCode()).append(BLANK);

        if (!StringUtils.isBlank(origin.getState())) {
            originAddress.append(origin.getState()).append(BLANK);
        }
        if (origin.getZone() != null) {
            originAddress.append(origin.getZone().getCode()).append(BLANK);
        }
        originAddress.append(origin.getCountry().getIsoCode());


        // Build destination address
        StringBuilder destinationAddress = new StringBuilder();

        destinationAddress.append(delivery.getAddress()).append(BLANK);
        if (!StringUtils.isBlank(delivery.getCity())) {
            destinationAddress.append(delivery.getCity()).append(BLANK);
        }
        destinationAddress.append(delivery.getPostalCode()).append(BLANK);

        if (!StringUtils.isBlank(delivery.getState())) {
            destinationAddress.append(delivery.getState()).append(BLANK);
        }
        if (delivery.getZone() != null) {
            destinationAddress.append(delivery.getZone().getCode()).append(BLANK);
        }
        destinationAddress.append(delivery.getCountry().getIsoCode());

        try {
            GeocodingResult[] originAddressResult = GeocodingApi.geocode(context,
                    originAddress.toString()).await();

            GeocodingResult[] destinationAddressResult = GeocodingApi.geocode(context,
                    destinationAddress.toString()).await();

            if (originAddressResult.length > 0 && destinationAddressResult.length > 0) {
                LatLng originLatLng = originAddressResult[0].geometry.location;
                LatLng destinationLatLng = destinationAddressResult[0].geometry.location;

                delivery.setLatitude(String.valueOf(destinationLatLng.lat));
                delivery.setLongitude(String.valueOf(destinationLatLng.lng));

                // Keep latlng for further usage in order to display the map

                DistanceMatrix distanceRequest = DistanceMatrixApi.newRequest(context)
                        .origins(new LatLng(originLatLng.lat, originLatLng.lng))
                        .destinations(new LatLng(destinationLatLng.lat, destinationLatLng.lng))
                        .awaitIgnoreError();

                if (distanceRequest != null) {
                    DistanceMatrixRow distanceMax = distanceRequest.rows[0];
                    Distance distance = distanceMax.elements[0].distance;
                    quote.getQuoteInformations().put(Constants.DISTANCE_KEY, 0.001 * distance.inMeters);
                } else {
                    LOGGER.error("Expected distance inner google api to return DistanceMatrix, it returned null. API key might not be working for this request");
                }
            }

        } catch (Exception e) {
            LOGGER.error("Exception while calculating the shipping distance", e);
        }
    }

    public String getModuleCode() {
        return MODULE_CODE;
    }
}
