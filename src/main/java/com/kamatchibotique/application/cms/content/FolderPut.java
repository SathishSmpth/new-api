/**
 * 
 */
package com.kamatchibotique.application.cms.content;

import java.util.Optional;

import com.kamatchibotique.application.exception.ServiceException;


public interface FolderPut {
	
	
  /**
   * Create folder on root or on specific path
   * @param merchantStoreCode
   * @param folderName
   * @param path
   * @throws ServiceException
   */
  void addFolder(final String merchantStoreCode, String folderName, Optional<String> path)
      throws ServiceException;

}
