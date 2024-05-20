package com.kamatchibotique.application.service.services.user;

import java.util.List;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.service.services.common.CommonService;
import com.kamatchibotique.application.model.user.Group;
import com.kamatchibotique.application.model.user.Permission;
import com.kamatchibotique.application.model.user.PermissionCriteria;
import com.kamatchibotique.application.model.user.PermissionList;



public interface PermissionService extends CommonService<Integer, Permission> {

  List<Permission> getByName();

  List<Permission> listPermission() throws ServiceException;

  Permission getById(Integer permissionId);

  List<Permission> getPermissions(List<Integer> groupIds) throws ServiceException;

  void deletePermission(Permission permission) throws ServiceException;

  PermissionList listByCriteria(PermissionCriteria criteria) throws ServiceException;

  void removePermission(Permission permission, Group group) throws ServiceException;

}
