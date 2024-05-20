package com.kamatchibotique.application.service.services.reference.init;

import com.kamatchibotique.application.exception.ServiceException;

public interface InitializationDatabase {
	
	boolean isEmpty();
	
	void populate(String name) throws ServiceException;

}
