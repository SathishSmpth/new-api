package com.kamatchibotique.application.cms.impl;

public class LocalCacheManagerImpl implements CMSManager {

  private String rootName;// file location root

  public LocalCacheManagerImpl(String rootName) {
    this.rootName = rootName;
  }


  @Override
  public String getRootName() {
    return rootName;
  }

  @Override
  public String getLocation() {
    return "";
  }


}
