package com.kamatchibotique.application.utils;

import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.exception.ServiceRuntimeException;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.service.services.reference.language.LanguageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static com.kamatchibotique.application.constants.Constants.DEFAULT_STORE;

@Component
public class LanguageUtils {

    protected final Log logger = LogFactory.getLog(getClass());
    public static final String REQUEST_PARAMATER_STORE = "store";

    private static final String ALL_LANGUALES = "_all";

    @Autowired
    LanguageService languageService;

//    @Autowired
//    private StoreFacade storeFacade;

    public Language getServiceLanguage(String lang) {
        Language l = null;
        if (!StringUtils.isBlank(lang)) {
            try {
                l = languageService.getByCode(lang);
            } catch (ServiceException e) {
                logger.error("Cannot retrieve language " + lang, e);
            }
        }

        if (l == null) {
            l = languageService.defaultLanguage();
        }

        return l;
    }

    /**
     * Determines request language based on store rules
     *
     * @param request
     * @return
     */
    public Language getRequestLanguage(HttpServletRequest request, HttpServletResponse response) {

        Locale locale = null;

        Language language = (Language) request.getSession().getAttribute(Constants.LANGUAGE);
        MerchantStore store =
                (MerchantStore) request.getSession().getAttribute(Constants.MERCHANT_STORE);


        if (language == null) {
            try {

                locale = LocaleContextHolder.getLocale();// should be browser locale


                if (store != null) {
                    language = store.getDefaultLanguage();
                    if (language != null) {
                        locale = languageService.toLocale(language, store);
                        if (locale != null) {
                            LocaleContextHolder.setLocale(locale);
                        }
                        request.getSession().setAttribute(Constants.LANGUAGE, language);
                    }

                    if (language == null) {
                        language = languageService.toLanguage(locale);
                        request.getSession().setAttribute(Constants.LANGUAGE, language);
                    }

                }

            } catch (Exception e) {
                if (language == null) {
                    try {
                        language = languageService.getByCode(Constants.DEFAULT_LANGUAGE);
                    } catch (Exception ignore) {
                    }
                }
            }
        } else {


            Locale localeFromContext = LocaleContextHolder.getLocale();// should be browser locale
            if (!language.getCode().equals(localeFromContext.getLanguage())) {
                // get locale context
                language = languageService.toLanguage(localeFromContext);
            }

        }

        if (language != null) {
            locale = languageService.toLocale(language, store);
        } else {
            language = languageService.toLanguage(locale);
        }

        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
        if (localeResolver != null) {
            localeResolver.setLocale(request, response, locale);
        }
        response.setLocale(locale);
        request.getSession().setAttribute(Constants.LANGUAGE, language);

        return language;
    }

    public Language getRESTLanguage(HttpServletRequest request, NativeWebRequest webRequest) {

        Objects.requireNonNull(request, "HttpServletRequest must not be null");

        try {
            Language language = null;

            String lang = request.getParameter(Constants.LANG);

            if (StringUtils.isBlank(lang)) {
                if (language == null) {
                    String storeValue = Optional.ofNullable(webRequest.getParameter(REQUEST_PARAMATER_STORE))
                            .filter(StringUtils::isNotBlank).orElse(DEFAULT_STORE);
                    if (!StringUtils.isBlank(storeValue)) {
                        try {
//                            MerchantStore storeModel = storeFacade.get(storeValue);
                            MerchantStore storeModel = null;
                            language = storeModel.getDefaultLanguage();
                        } catch (Exception e) {
                            logger.warn("Cannot get store with code [" + storeValue + "]");
                        }

                    } else {
                        language = languageService.defaultLanguage();
                    }

                }
            } else {
                if (!ALL_LANGUALES.equals(lang)) {
                    language = languageService.getByCode(lang);
                    if (language == null) {
                        language = languageService.defaultLanguage();
                    }
                }
            }

            //if language is null then underlying facade must load all languages
            return language;

        } catch (ServiceException e) {
            throw new ServiceRuntimeException(e);
        }
    }

}
