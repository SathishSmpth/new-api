package com.kamatchibotique.application.utils;

import com.kamatchibotique.application.model.common.Address;

public interface GeoLocation {
	
	Address getAddress(String ipAddress) throws Exception;

}
