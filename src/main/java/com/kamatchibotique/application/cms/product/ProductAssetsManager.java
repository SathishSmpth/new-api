package com.kamatchibotique.application.cms.product;

import java.io.Serializable;

import com.kamatchibotique.application.cms.common.AssetsManager;

public interface ProductAssetsManager
    extends AssetsManager, ProductImageGet, ProductImagePut, ProductImageRemove, Serializable {

}
