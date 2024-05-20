package com.kamatchibotique.application.cms.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component("gcpAssetsManager")
public class GCPCacheManagerImpl implements CMSManager {

  @Value("${config.cms.gcp.bucket}")
  private String bucketName;

  @Override
  public String getRootName() {
    return bucketName;
  }

  public String getBucketName() {
    return bucketName;
  }


  @Override
  public String getLocation() {
    return null;
  }
  
  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }




}
