package com.kamatchibotique.application.repository.user;

import com.kamatchibotique.application.model.user.PermissionCriteria;
import com.kamatchibotique.application.model.user.PermissionList;




public interface PermissionRepositoryCustom {

	PermissionList listByCriteria(PermissionCriteria criteria);


}
