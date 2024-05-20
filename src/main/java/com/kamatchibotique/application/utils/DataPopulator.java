/**
 * 
 */
package com.kamatchibotique.application.utils;

import com.kamatchibotique.application.exception.ConversionException;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;

/**
 * @author Umesh A
 *
 */
public interface DataPopulator<Source,Target> {

    Target populate(Source source,Target target, MerchantStore store, Language language) throws ConversionException;
    Target populate(Source source, MerchantStore store, Language language) throws ConversionException;

}
