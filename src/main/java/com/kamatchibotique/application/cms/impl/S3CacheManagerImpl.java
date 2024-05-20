package com.kamatchibotique.application.cms.impl;

public class S3CacheManagerImpl implements CMSManager {


  private String bucketName;
  private String regionName;

  public S3CacheManagerImpl(String bucketName, String regionName) {
    this.bucketName = bucketName;
    this.regionName = regionName;
  }


  @Override
  public String getRootName() {
    return bucketName;
  }

  @Override
  public String getLocation() {
    return regionName;
  }


  public String getBucketName() {
    return bucketName;
  }

  public String getRegionName() {
    return regionName;
  }


}
