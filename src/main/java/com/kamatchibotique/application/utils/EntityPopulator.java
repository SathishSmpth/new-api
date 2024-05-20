/**
 * 
 */
package com.kamatchibotique.application.utils;

import com.kamatchibotique.application.exception.ConversionException;
import com.kamatchibotique.application.model.merchant.MerchantStore;

/**
 * @author Umesh A
 *
 */
public interface EntityPopulator<Source,Target>
{

    Target populateToEntity(Source source, Target target, MerchantStore store)  throws ConversionException;
    Target populateToEntity(Source source) throws ConversionException;
}
