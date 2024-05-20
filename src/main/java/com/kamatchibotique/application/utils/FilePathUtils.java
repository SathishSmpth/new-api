package com.kamatchibotique.application.utils;

import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.enums.FileContentType;
import com.kamatchibotique.application.model.catalog.product.file.DigitalProduct;
import com.kamatchibotique.application.model.merchant.MerchantStore;
//import com.kamatchibotique.application.model.order.ReadableOrderProductDownload;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Properties;

import static com.kamatchibotique.application.constants.Constants.*;

@Component
public class FilePathUtils {

    private static final String DOWNLOADS = "/downloads/";
    private static final String DOUBLE_SLASH = "://";
    private static final String CONTEXT_PATH = "CONTEXT_PATH";
    public static final String X_FORWARDED_HOST = "X-Forwarded-Host";
    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

    @Autowired
    private CoreConfiguration coreConfiguration;

    @Autowired
    @Qualifier("img")
    private ImageFilePath imageUtils;

    @Resource(name = "shopizer-properties")
    public Properties properties = new Properties();

    public String buildStaticFilePath(String storeCode, String fileName) {
        String path = FILES_URI + SLASH + storeCode + SLASH;
        if (StringUtils.isNotBlank(fileName)) {
            return path + fileName;
        }
        return path;
    }

    public String buildStaticFilePath(MerchantStore store) {
        return STATIC_URI + FILES_URI + SLASH + store.getCode() + SLASH;
    }

    public String buildAdminDownloadProductFilePath(MerchantStore store, DigitalProduct digitalProduct) {
        return ADMIN_URI + FILES_URI + DOWNLOADS + store.getCode() + SLASH + digitalProduct.getProductFileName();
    }

//    public String buildOrderDownloadProductFilePath(MerchantStore store, ReadableOrderProductDownload digitalProduct,
//                                                    Long orderId) {
//        return SHOP_URI + ORDER_DOWNLOAD_URI + SLASH + orderId + SLASH + digitalProduct.getId() + URL_EXTENSION;
//    }

    public String buildStaticFileAbsolutePath(MerchantStore store, String fileName) {
        if (StringUtils.isNotBlank(imageUtils.getBasePath(store))
                && imageUtils.getBasePath(store).startsWith(HTTP_SCHEME)) {
            return imageUtils.getBasePath(store) + FILES_URI + SLASH + store.getCode() + SLASH
                    + FileContentType.STATIC_FILE + SLASH + fileName;
        } else {
            String scheme = this.getScheme(store);
            return scheme + SLASH + coreConfiguration.getProperty("CONTEXT_PATH")
                    + buildStaticFilePath(store.getCode(), fileName);
        }
    }


    public String buildStoreUri(MerchantStore store, HttpServletRequest request) {
        return buildBaseUrl(request, store);
    }

    public String buildStoreUri(MerchantStore store, String contextPath) {
        return normalizePath(contextPath);
    }

    public String buildRelativeStoreUri(HttpServletRequest request, MerchantStore store) {
        return "" + normalizePath(request.getContextPath());
    }

    public String buildCustomerUri(MerchantStore store, String contextPath) {
        return buildStoreUri(store, contextPath);
    }

    public String buildAdminUri(MerchantStore store, HttpServletRequest request) {
        String baseUrl = buildBaseUrl(request, store);
        return baseUrl + ADMIN_URI;
    }

    public String buildCategoryUrl(MerchantStore store, String contextPath, String url) {
        return buildStoreUri(store, contextPath) + SHOP_URI + CATEGORY_URI + SLASH + url + URL_EXTENSION;
    }

    public String buildProductUrl(MerchantStore store, String contextPath, String url) {
        return buildStoreUri(store, contextPath) + SHOP_URI + Constants.PRODUCT_URI + SLASH + url + URL_EXTENSION;
    }

    public String getContextPath() {
        return properties.getProperty(CONTEXT_PATH);
    }

    private String normalizePath(String path) {
        if (SLASH.equals(path)) {
            return BLANK;
        } else {
            return path;
        }
    }

    private String getDomainName(String domainName) {
        if (StringUtils.isBlank(domainName)) {
            return DEFAULT_DOMAIN_NAME;
        } else {
            return domainName;
        }
    }

    private String getScheme(MerchantStore store) {
        String baseScheme = store.getDomainName();
        if (baseScheme != null && baseScheme.length() > 0
                && baseScheme.charAt(baseScheme.length() - 1) == Constants.SLASH.charAt(0)) {
            baseScheme = baseScheme.substring(0, baseScheme.length() - 1);
        }
        // end no more
        return validUrl(baseScheme);
    }

    public String validUrl(final String url) {
        if (!StringUtils.isBlank(url) && !url.startsWith(HTTP) && !url.startsWith(HTTP)) {
            return HTTPS + url;
        }
        return url;
    }

    private String buildBaseUrl(HttpServletRequest request, MerchantStore store) {
        String contextPath = normalizePath(request.getContextPath());
        String scheme = getScheme(store);
        return scheme + DOUBLE_SLASH + contextPath;
    }

    public String buildBaseUrl(String contextPath, MerchantStore store) {
        String normalizePath = normalizePath(contextPath);
        String scheme = getScheme(store);
        return scheme + SLASH + normalizePath;
    }

    public String buildStoreForwardedUri(MerchantStore merchantStore, HttpServletRequest request) {
        String uri;
        if (StringUtils.isNotEmpty(request.getHeader(X_FORWARDED_HOST))) {
            uri = request.getHeader(X_FORWARDED_HOST);
        } else {
            uri = buildStoreUri(merchantStore, request);
        }
        return uri;
    }

    public boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
