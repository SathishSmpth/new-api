package com.kamatchibotique.application.cms.impl;

public class DownloadCacheManagerImpl extends CacheManagerImpl {


  private final static String NAMED_CACHE = "DownlaodRepository";
  private String root;


  public DownloadCacheManagerImpl(String location, String root) {
    super.init(NAMED_CACHE, location);
    this.root = root;
  }


  @Override
  public String getRootName() {
    return root;
  }


  @Override
  public String getLocation() {
    return location;
  }



}

