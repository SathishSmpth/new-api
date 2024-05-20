package com.kamatchibotique.application.service.services.shipping;

import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.shipping.ShippingOrigin;

/**
 * ShippingOrigin object if different from MerchantStore address.
 * Can be managed through this service.
 * @author carlsamson
 *
 */
public interface ShippingOriginService  extends CommonService<Long, ShippingOrigin> {

	ShippingOrigin getByStore(MerchantStore store);



}
