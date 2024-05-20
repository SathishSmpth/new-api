package com.kamatchibotique.application.cms.content;

import java.util.List;
import java.util.Optional;

import com.kamatchibotique.application.exception.ServiceException;

public interface FolderList {
	
	  List<String> listFolders(final String merchantStoreCode, Optional<String> path)
		      throws ServiceException;

}
