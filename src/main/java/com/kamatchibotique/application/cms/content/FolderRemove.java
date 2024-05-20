/**
 * 
 */
package com.kamatchibotique.application.cms.content;

import java.util.Optional;

import com.kamatchibotique.application.exception.ServiceException;


public interface FolderRemove {
  void removeFolder(final String merchantStoreCode, String folderName, Optional<String> folderPath)
      throws ServiceException;

}
